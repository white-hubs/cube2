package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseAppealMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseAppealPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseAppealQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseAppealQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseAppealQueryServiceImpl extends ServiceImpl<EnterpriseAppealMapper, EnterpriseAppealPO> implements EnterpriseAppealQueryService {

    private final TypeDataQueryService typeDataQueryService;
    private final FileClient fileClient;
    private final EnterpriseMapper enterpriseMapper;

    @Override
    public EnterpriseAppealVO getVoById(Long id) {
        EnterpriseAppealPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的企业诉求");

        EnterpriseAppealVO vo = ObjectUtils.copying(po, EnterpriseAppealVO.class);
        // 查询 诉求类型名称
        vo.setAppealTypeName(this.getAppealTypeName(vo.getAppealTypeId()));
        // 查询 文件
        this.extendFileVOS(vo);

        EnterprisePO enterprisePO = enterpriseMapper.selectById(vo.getEnterpriseId());
        vo.setEnterpriseName(enterprisePO.getName());
        return vo;
    }

    @Override
    public List<EnterpriseAppealListVO> getVOList(EnterpriseAppealQuery query) {

        List<EnterpriseAppealPO> pos = this.getQueryChainWrapper(query).list();
        List<EnterpriseAppealListVO> vos = ObjectUtils.copying(pos, EnterpriseAppealListVO.class);

        return vos;
    }

    @Override
    public List<EnterpriseAppealListVO> getVOListByIds(List<Long> ids) {
        List<EnterpriseAppealListVO> vos = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<EnterpriseAppealPO> pos = this.listByIds(ids);
            vos = ObjectUtils.copying(pos, EnterpriseAppealListVO.class);
        }
        return vos;
    }

    @Override
    public Pagination<EnterpriseAppealListVO> getVOPage(EnterpriseAppealQuery query) {
        Page<EnterpriseAppealPO> page = this.getQueryChainWrapper(query)
                .page(PagingUtils.convert(query));

        List<EnterpriseAppealPO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<EnterpriseAppealListVO> vos = ObjectUtils.copying(pos, EnterpriseAppealListVO.class);
        vos.parallelStream().forEach(vo -> {
            vo.setAppealTypeName(this.getAppealTypeName(vo.getAppealTypeId()));
            if (StringUtils.isEmpty(vo.getEnterpriseName())) {
                EnterprisePO enterprisePO = enterpriseMapper.selectById(vo.getEnterpriseId());
                vo.setEnterpriseName(enterprisePO.getName());
            }

        });

        return PagingUtils.convert(page, vos);
    }

    private LambdaQueryChainWrapper<EnterpriseAppealPO> getQueryChainWrapper(EnterpriseAppealQuery query) {
        return this.lambdaQuery()
                .eq(null != query.getAppealTypeId(), EnterpriseAppealPO::getAppealTypeId, query.getAppealTypeId())
                .eq(null != query.getEnterpriseId(), EnterpriseAppealPO::getEnterpriseId, query.getEnterpriseId())
                .between(query.getPeriod() !=
                         null, EnterpriseAppealPO::getTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .and(StringUtils.isNotEmpty(query.getKeyword()), wrapper -> wrapper.like(EnterpriseAppealPO::getName, query.getKeyword())
                        .or().like(EnterpriseAppealPO::getEnterpriseName, query.getKeyword()))
                .orderByDesc(EnterpriseAppealPO::getTime);

    }

    private String getAppealTypeName(Long appealTypeId) {
        String name = "";
        if (appealTypeId != null) {
            TypeDataPO typeDataPO = typeDataQueryService.getById(appealTypeId);
            name = typeDataPO != null ? typeDataPO.getName() : "";
        }
        return name;
    }

    private void extendFileVOS(EnterpriseAppealVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询文件异常", e);
        }

    }

}
