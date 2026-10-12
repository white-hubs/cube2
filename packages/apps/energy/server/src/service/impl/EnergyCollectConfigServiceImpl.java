package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：能耗采集配置服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyCollectConfigServiceImpl extends ServiceImpl<EnergyCollectConfigMapper, EnergyCollectConfig> implements EnergyCollectConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnergyCollectConfigParam param) {
        EnergyCollectConfig energyCollectConfig = ObjectUtils.copying(param, EnergyCollectConfig.class);
        this.baseMapper.insert(energyCollectConfig);
        return energyCollectConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EnergyCollectConfigParam param) {
        EnergyCollectConfig energyCollectConfig = this.baseMapper.selectById(id);
        Assert.isNotFound(energyCollectConfig, "未查询到相关信息");
        //前端没有传code过来 这个code本身是不能变的
        param.setCategoryCode(energyCollectConfig.getCategoryCode());
        ObjectUtils.copying(param, energyCollectConfig);
        this.baseMapper.updateById(energyCollectConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EnergyCollectConfigVO getById(Long id) {
        EnergyCollectConfig energyCollectConfig = this.baseMapper.selectById(id);
        return ObjectUtils.copying(energyCollectConfig, EnergyCollectConfigVO.class);
    }

    @Override
    public Pagination<EnergyCollectConfigVO> getPage(EnergyCollectConfigQueryParam param) {
        //请自行添加查询条件
        Page<EnergyCollectConfigVO> page = baseMapper.getEnergyCollectConfigFuelPage(PagingUtils.convert(param), Wrappers.emptyWrapper());
        //处理初始化问题
        if (CollectionUtils.isEmpty(page.getRecords())) {
            //初始化数据
            Arrays.stream(InstrumentTypeEnum.values()).forEach(it -> {
                if (!it.getCode().equals(InstrumentTypeEnum.ENERGY_METER.getCode())
                    && !it.getCode().equals(InstrumentTypeEnum.VAPOUR.getCode())) {
                    this.add(new EnergyCollectConfigParam(it.getCode(), it.getName(), "auto", true, BizCodeUtils.generate("CF"), null, null, null));
                }
            });
            page = baseMapper.getEnergyCollectConfigFuelPage(PagingUtils.convert(param), Wrappers.emptyWrapper());
        }
        return PagingUtils.convert(page, EnergyCollectConfigVO.class);
    }

    @Override
    public Map<String, EnergyCollectConfig> getCategoryMap() {
        List<EnergyCollectConfig> list = this.list();
        Map<String, EnergyCollectConfig> map = list.stream().collect(Collectors.toMap(EnergyCollectConfig::getCategoryCode, n -> n));
        return map;
    }

    @Override
    public List<EnergyCollectConfigVO> getList(EnergyCollectConfigQueryParam param) {
        return this.baseMapper.getEnergyCollectConfigFuelList(Wrappers.<EnergyCollectConfig>lambdaQuery()
                                                                      .like(StringUtils.isNotBlank(param.getCategoryName()), EnergyCollectConfig::getCategoryName, param.getCategoryName())
                                                                      .like(StringUtils.isNotBlank(param.getCategoryCode()), EnergyCollectConfig::getCategoryCode, param.getCategoryCode())
                                                             );
    }
}
