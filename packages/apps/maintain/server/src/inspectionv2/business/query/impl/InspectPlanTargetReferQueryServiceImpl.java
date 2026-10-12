package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanTargetReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanTargetReferMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanTargetReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanTargetReferVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanTargetReferQueryServiceImpl extends ServiceImpl<InspectPlanTargetReferMapper, InspectPlanTargetReferPO> implements InspectPlanTargetReferQueryService {

    private final InspectStandardQueryService standardQueryService;
    private final InspectRecordQueryService recordQueryService;

    private final InspectTargetInfoQuery targetInfoQuery;

    @Override
    public List<InspectPlanTargetReferVO> getVOByPlanId(Long planId) {
        return this.getPlanTargetReferVOS(planId, null);
    }

    @Override
    public InspectPlanTargetReferVO getVOByPlanIdAndTargetId(Long planId, Long targetId, Long taskId) {

        List<InspectPlanTargetReferVO> vos = this.getPlanTargetReferVOS(planId, targetId);
        InspectPlanTargetReferVO vo = null;
        if (vos.size() > 0) {
            vo = vos.get(0);

            if (vo.getStandardIds().size() > 0) {
                vo.setStandardVOS(standardQueryService.getListWithResult(vo.getStandardIds()));
            }

            if (targetId != null) {
                vo.setRecordId(this.getRecordIdByTargetIdAndTaskId(targetId, taskId));

            }
        }

        return vo;

    }

    private Long getRecordIdByTargetIdAndTaskId(Long targetId, Long taskId) {
        Long recordId = null;
        InspectRecordPO po = recordQueryService.lambdaQuery()
                .eq(InspectRecordPO::getTargetId, targetId)
                .eq(InspectRecordPO::getTaskId, taskId)
                .select(InspectRecordPO::getId)
                .one();

        if (po != null) {
            recordId = po.getId();
        }
        return recordId;
    }

    private List<InspectPlanTargetReferVO> getPlanTargetReferVOS(Long planId, Long targetId) {
        List<InspectPlanTargetReferVO> vos = new ArrayList<>();

        List<InspectPlanTargetReferPO> pos = this.lambdaQuery()
                .eq(InspectPlanTargetReferPO::getPlanId, planId)
                .eq(targetId != null, InspectPlanTargetReferPO::getTargetId, targetId)
                .list();

        Map<InspectTargetCategoryEnum, List<InspectPlanTargetReferPO>> objectGroups = Optional.ofNullable(pos)
                .orElse(Collections.emptyList())
                .stream().collect(Collectors.groupingBy(InspectPlanTargetReferPO::getTargetCategory));

        objectGroups.forEach((targetCategoryEnum, targetReferPOS) -> {
            List<Long> targetIds = targetReferPOS.stream()
                    .map(InspectPlanTargetReferPO::getTargetId)
                    .collect(Collectors.toList());
            Map<Long, String> targetIdStandardIdsMap = targetReferPOS.stream()
                    .collect(Collectors.toMap(InspectPlanTargetReferPO::getTargetId, InspectPlanTargetReferPO::getStandardIds));

            List<InspectTargetInfoDTO> infoDTOS = targetInfoQuery.getInspectObject(targetCategoryEnum.name(), targetIds, targetIdStandardIdsMap);
            vos.addAll(ObjectUtils.copying(infoDTOS, InspectPlanTargetReferVO.class));
        });
        return vos;
    }
}
