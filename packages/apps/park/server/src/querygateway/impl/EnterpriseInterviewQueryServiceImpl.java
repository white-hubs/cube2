package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseInterviewMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseInterviewPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseAppealQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseInterviewQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseInterviewQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseInterviewQueryServiceImpl extends ServiceImpl<EnterpriseInterviewMapper, EnterpriseInterviewPO> implements EnterpriseInterviewQueryService {

    private final TypeDataQueryService typeDataQueryService;
    private final EnterpriseAppealQueryService appealQueryService;
    private final FileClient fileClient;
    private final EnterpriseMapper enterpriseMapper;

    @Override
    public EnterpriseInterviewVO getVoById(Long id) {
        EnterpriseInterviewPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的走访信息");

        EnterpriseInterviewVO vo = ObjectUtils.copying(po, EnterpriseInterviewVO.class);
        // 查询 诉求类型名称
        vo.setInterviewTypeName(this.getInterviewTypeName(vo.getInterviewTypeId()));

        // 查询 企业诉求信息
        this.extendAppealListVOS(vo);

        // 查询 文件
        this.extendFileVOS(vo);

        EnterprisePO enterprisePO = enterpriseMapper.selectById(vo.getEnterpriseId());
        vo.setEnterpriseName(enterprisePO.getName());
        return vo;
    }

    @Override
    public List<EnterpriseInterviewListVO> getVOList(EnterpriseInterviewQuery query) {

        List<EnterpriseInterviewPO> pos = this.getQueryChainWrapper(query).list();
        List<EnterpriseInterviewListVO> vos = ObjectUtils.copying(pos, EnterpriseInterviewListVO.class);

        return vos;
    }

    @Override
    public Pagination<EnterpriseInterviewListVO> getVOPage(EnterpriseInterviewQuery query) {
        Page<EnterpriseInterviewPO> page = this.getQueryChainWrapper(query)
                .page(PagingUtils.convert(query));

        List<EnterpriseInterviewPO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<EnterpriseInterviewListVO> vos = ObjectUtils.copying(pos, EnterpriseInterviewListVO.class);
        vos.parallelStream().forEach(vo -> {
            vo.setInterviewTypeName(this.getInterviewTypeName(vo.getInterviewTypeId()));
            EnterprisePO enterprisePO = enterpriseMapper.selectById(vo.getEnterpriseId());
            if (Objects.nonNull(enterprisePO)) {
                vo.setEnterpriseName(enterprisePO.getName());
            }

        });

        return PagingUtils.convert(page, vos);
    }

    private LambdaQueryChainWrapper<EnterpriseInterviewPO> getQueryChainWrapper(EnterpriseInterviewQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), EnterpriseInterviewPO::getEnterpriseName, query.getKeyword())
                .eq(null != query.getInterviewTypeId(), EnterpriseInterviewPO::getInterviewTypeId, query.getInterviewTypeId())
                .eq(null != query.getEnterpriseId(), EnterpriseInterviewPO::getEnterpriseId, query.getEnterpriseId())
                .between(query.getPeriod() !=
                         null, EnterpriseInterviewPO::getTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .orderByDesc(EnterpriseInterviewPO::getTime);

    }

    private String getInterviewTypeName(Long interviewTypeId) {
        String name = "";
        if (interviewTypeId != null) {
            TypeDataPO typeDataPO = typeDataQueryService.getById(interviewTypeId);
            name = typeDataPO != null ? typeDataPO.getName() : "";
        }
        return name;
    }

    private void extendAppealListVOS(EnterpriseInterviewVO vo) {
        if (StringUtils.isNotEmpty(vo.getAppealIds())) {
            List<EnterpriseAppealListVO> appealListVOS = appealQueryService.getVOListByIds(Arrays.stream(vo.getAppealIds().split(","))
                                                                                                   .map(Long::parseLong)
                                                                                                   .collect(Collectors.toList()));
            vo.setAppealListVOS(appealListVOS);
        }
    }

    private void extendFileVOS(EnterpriseInterviewVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询文件异常", e);
        }

    }

}
