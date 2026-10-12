package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanTargetReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanTargetReferMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanTargetReferCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanTargetReferPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanTargetReferCommandServiceImpl extends ServiceImpl<InspectPlanTargetReferMapper, InspectPlanTargetReferPO> implements InspectPlanTargetReferCommandService {

    @Override
    public void saveOrUpdate(Long planId, List<InspectPlanTargetReferCommandModel> commandModels) {
        //删除之前的关联信息
        this.lambdaUpdate()
                .eq(InspectPlanTargetReferPO::getPlanId, planId)
                .remove();
        //保存最新的任务关联
        Set<InspectPlanTargetReferPO> pos = commandModels.stream()
                .map(commandModel -> InspectPlanTargetReferPO.builder()
                        .planId(planId)
                        .targetCategory(commandModel.getTargetCategory())
                        .targetType(commandModel.getTargetType())
                        .targetId(commandModel.getTargetId())
                        .targetName(commandModel.getTargetName())
                        .standardIds(String.join(",", commandModel.getStandardIds()))
                        .build()
                    ).collect(Collectors.toSet());

        this.saveBatch(pos);
    }
}
