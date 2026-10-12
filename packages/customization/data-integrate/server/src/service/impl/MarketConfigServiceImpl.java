package com.guhecloud.cube.base.dataintegrate.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.dataintegrate.server.dao.MarketConfigMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.MarketConfig;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigParam;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.cube.base.dataintegrate.server.vo.MarketConfigVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：采集市场配置服务层
 *
 * @author Appet
 * @date 2023-09-09 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketConfigServiceImpl extends ServiceImpl<MarketConfigMapper, MarketConfig> implements MarketConfigService {
    private final FileClient fileClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MarketConfigParam param) {
        MarketConfig marketConfig = ObjectUtils.copying(param, MarketConfig.class);
        MarketConfig exist = this.getByTenantId(param.getMarketTenant());
        Assert.isParameterError(Objects.nonNull(exist), "租户编号已存在");
        this.baseMapper.insert(marketConfig);
        return marketConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MarketConfigParam param) {
        MarketConfig marketConfig = this.baseMapper.selectById(id);
        Assert.isNotFound(marketConfig, "未查询到相关信息");
        MarketConfig exist = this.getByTenantId(param.getMarketTenant());
        Assert.isParameterError(Objects.nonNull(exist) && !exist.getId().equals(id), "租户编号已存在");
        ObjectUtils.copying(param, marketConfig);
        this.baseMapper.updateById(marketConfig);
    }

    public MarketConfig getByTenantId(Integer tenantId) {
        return this.baseMapper.selectOne(Wrappers.<MarketConfig>lambdaQuery()
                                                 .eq(MarketConfig::getMarketTenant, tenantId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public MarketConfigVO getById(Long id) {
        MarketConfig marketConfig = this.baseMapper.selectOne(Wrappers.<MarketConfig>lambdaQuery()
                                                                      .and(q -> q.eq(MarketConfig::getId, id)
                                                                              .or().eq(MarketConfig::getMarketTenant, id)));
        Assert.isNotFound(marketConfig, "未找到市场配置信息");
        return convertMarketConfigVO(marketConfig);
    }

    private MarketConfigVO convertMarketConfigVO(MarketConfig marketConfig) {
        MarketConfigVO vo = ObjectUtils.copying(marketConfig, MarketConfigVO.class);
        //查询文件
        if (StringUtils.isNotBlank(marketConfig.getFileIds())) {
            List<FileVO> files = fileClient.getByIds(Arrays.stream(marketConfig.getFileIds().split(",")).map(Long::parseLong)
                                                             .collect(Collectors.toList()));
            vo.setFiles(files);
        }
        return vo;
    }

    @Override
    public Pagination<MarketConfigVO> getPage(MarketConfigQueryParam param) {
        //请自行添加查询条件
        Page<MarketConfig> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), MarketConfig::getMarketName, param.getKeyword())
                .orderByDesc(Objects.nonNull(param.getStatusOrder()) && param.getStatusOrder(), MarketConfig::getStatus)
                .page(PagingUtils.convert(param));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MarketConfigVO> collects = page.getRecords().stream().map(this::convertMarketConfigVO).collect(Collectors.toList());
            return PagingUtils.convert(page, collects);
        }
        return Pagination.empty();
    }

    @Override
    public ContextParam getContextParam(Long id) {
        var marketConfigVO = this.baseMapper.selectById(id);
        Assert.isParameterError(!(StringUtils.isNotEmpty(marketConfigVO.getAccount()) &&
                                  StringUtils.isNotEmpty(marketConfigVO.getPassword()) &&
                                  marketConfigVO.getMarketTenant() != null), "市场配置信息不完善，无法采集数据");
        var token = this.getTokenClient(marketConfigVO.getUrl(), marketConfigVO.getAccount(), marketConfigVO.getPassword(), marketConfigVO.getMarketTenant());
        var contextParam = ObjectUtils.copying(marketConfigVO, ContextParam.class);
        contextParam.setToken(token);
        return contextParam;
    }

    private String getTokenClient(String urlPre, String account, String password, Integer marketTenant) {
        var url = (urlPre.endsWith("/") ? urlPre : urlPre + "/") + "api/ouaa/oauth2/token";
        MultiValueMap<String, Object> loginParam = new LinkedMultiValueMap<>();
        loginParam.add("grant_type", "client_credentials");
        loginParam.add("client_id", account);
        loginParam.add("client_secret", password);

        Map<String, String> authMap = new HashMap<>();
        authMap.put("terminal", "bimops-manage-web");
        try {
            JsonNode jsonNode = HttpUtils.restPost(url, loginParam, authMap, MediaType.MULTIPART_FORM_DATA, new TypeReference<>() {});
            return JsonUtils.getNodeValue(jsonNode, "access_token");
        } catch (Exception e) {
            log.error("数据采集获取认证信息失败：", e);
            return null;
        }
    }
}