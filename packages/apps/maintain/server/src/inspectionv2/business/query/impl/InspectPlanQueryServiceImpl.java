package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanTargetReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanQueryServiceImpl extends ServiceImpl<InspectPlanMapper, InspectPlanPO> implements InspectPlanQueryService {

    private final InspectQuery inspectQuery;

    private final InspectPlanTargetReferQueryService targetReferQueryService;

    private final HandlerService handlerService;

    private final PlanRelationService planRelationService;

    @Override
    public InspectPlanVO getVOById(Long id) {

        InspectPlanPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检计划");
        //通过任务ID 查询任务关联的对象 也就是巡检点
        InspectPlanVO vo = ObjectUtils.copying(po, InspectPlanVO.class);
        vo.setPlanTargets(targetReferQueryService.getVOByPlanId(id));
        vo.setReceiveNames(inspectQuery.extendUserNames(vo.getTenant(), vo.getReceiveIds()));
        vo.setReceiveDeptNames(inspectQuery.extendDeptNames(vo.getTenant(), vo.getReceiveDeptIds()));
        vo.setReceivePostNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceivePostIds()));
        vo.setReceiveRoleNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceiveRoleIds()));
        // 设置检查人名称
        if ("2".equals(po.getCheckUserType())) {
            vo.setCheckUserNames(handlerService.handlerDeptIdsAndPostIdsToNames(po.getCheckDeptIds(), po.getCheckPostIds()));
        } else {
            vo.setCheckUserNames(handlerService.handlerUserIdsToNames(po.getCheckUserIds()));
        }
        return vo;
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     *
     * @param plainId
     * @param targetId
     *
     * @return
     */
    @Override
    public List<StandardResult> getDeailStandardList(Long plainId, Long targetId) {
        return planRelationService.getStandardList(plainId, targetId);
    }

    @Override
    public Pagination<InspectPlanVO> getPage(InspectPlanQueryParam param) {
        Page<InspectPlanPO> page = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(InspectPlanPO::getName, param.getKeyword())
                    )
                .eq(param.getTypeId() != null, InspectPlanPO::getTypeId, param.getTypeId())
                .eq(param.getStatus() != null, InspectPlanPO::getStatus, param.getStatus())
                .orderByDesc(InspectPlanPO::getCreateTime)
                .page(PagingUtils.convert(param));

        Pattern pattern = Pattern.compile("[0-9]*");
        List<InspectPlanVO> vos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .parallelStream().map(po -> {
                    InspectPlanVO vo = ObjectUtils.copying(po, InspectPlanVO.class);
                    vo.setReceiveNames(inspectQuery.extendUserNames(vo.getTenant(), vo.getReceiveIds()));
                    vo.setReceiveDeptNames(inspectQuery.extendDeptNames(vo.getTenant(), vo.getReceiveDeptIds()));
                    vo.setReceivePostNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceivePostIds()));
                    vo.setReceiveRoleNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceiveRoleIds()));
                    // 设置创建人名称
                    if (StringUtils.isNotEmpty(vo.getCreateBy()) && pattern.matcher(vo.getCreateBy()).matches()) {
                        if (StringUtils.isNotBlank(vo.getCreatorId()) && ActorTypeEnum.USER.name().equals(vo.getCreatorType())) {
                            vo.setCreateByName(handlerService.getUserNameById(Long.valueOf(vo.getCreatorId())));
                        } else {
                            vo.setCreateByName(handlerService.getUserNameByName(vo.getCreateBy()));
                        }
                    }
                    return vo;
                }).collect(Collectors.toList());
        return PagingUtils.convert(page, vos);
    }

    @Override
    public String getPlanName(Long id) {
        if (id == null) {
            return "";
        }
        var po = this.getById(id);
        return po == null ? "" : po.getName();
    }

}
