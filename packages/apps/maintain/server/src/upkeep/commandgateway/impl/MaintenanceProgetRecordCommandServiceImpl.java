package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceProgetRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceProgetRecordCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceTasksCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceProgetRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceTasksMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenaceVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProgetRecord;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenancePlanService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceProgetRecordService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.produce.MaintainEventProduce;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceProgetRecordCommandServiceImpl extends ServiceImpl<MaintenanceProgetRecordMapper, MaintenanceProgetRecord> implements MaintenanceProgetRecordCommandService {

    private final MaintenanceTasksMapper maintenanceTasksMapper;
    private final MaintenancePlanService maintenancePlanService;
    private final MaintenanceProgetRecordService maintenanceProgetRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProgetRecord(@Valid MaintenanceTasksCommand maintenanceTasksCommand) throws ParseException {
        //维保项记录
        List<MaintenanceProgetRecordCommand> maintenanceProgetRecordCommands = maintenanceTasksCommand.getMaintenanceProgetRecordCommands();
        LocalDateTime now = LocalDateTime.now();
        //维保任務
        Principal principal = SecurityContextHolder.getPrincipal();
        MaintenanceTasks maintenanceTasks = maintenanceTasksMapper.selectById(maintenanceTasksCommand.getId());
        maintenanceTasks.setMaintenanceFinishTime(now);
        maintenanceTasks.setMaintenanceTaskStatus(MaintenaceTaskStatusEnum.COMPLETED.name());
        maintenanceTasks.setMaintenanceTaskPerson(principal.getName());
        maintenanceTasks.setMaintenanceTaskPersonId(principal.getId());
        maintenanceTasks.setPicture(maintenanceTasksCommand.getPicture());
        maintenanceTasks.setTaskRemark(maintenanceTasksCommand.getTaskRemark());
        List<String> fileIds = maintenanceTasksCommand.getFileIds();
        if (!fileIds.isEmpty()) {
            String fileIdStr = String.join("-", fileIds);
            maintenanceTasks.setPicture(fileIdStr);
        }
        maintenanceTasksMapper.updateById(maintenanceTasks);
        if (ObjectUtils.isNotEmpty(maintenanceProgetRecordCommands)) {
            maintenanceProgetRecordCommands.forEach(command -> {
                maintenanceProgetRecordService.lambdaUpdate()
                        .set(MaintenanceProgetRecord::getRecord, command.getRecord())
                        .set(MaintenanceProgetRecord::getMaintenanceDescribe, command.getMaintenanceDescribe())
                        .eq(MaintenanceProgetRecord::getId, command.getId()).update();
            });
        }
        //推送设备生命周期信息
        MaintenancePlan plan = maintenancePlanService.getById(maintenanceTasks.getPlanId());
        MaintenaceVo maintenaceVo = new MaintenaceVo();
        maintenaceVo.setTenant(plan.getTenant());
        maintenaceVo.setId(maintenanceTasks.getId());
        maintenaceVo.setName(plan.getMaintenancePlanName());
        maintenaceVo.setStatus(MaintenaceTaskStatusEnum.COMPLETED.name());
        maintenaceVo.setStatusName(MaintenaceTaskStatusEnum.COMPLETED.getDescription());
        MaintainEventProduce.sendFaultEvent(maintenaceVo);

        //代办完成
        completePush(maintenaceVo);

    }

    //完成操作
    private void completePush(MaintenaceVo maintenaceVo) {
        maintenaceVo.setStatus(MaintenaceTaskStatusEnum.BE_MAINTAINED.name());
        TodoTaskFlowCirculationMessage build = TodoTaskFlowCirculationMessage.builder()
                .businessKey(String.valueOf(maintenaceVo.getId()))
                .businessStatus(MaintenaceTaskStatusEnum.BE_MAINTAINED.name())
                .handlerTime(LocalDateTime.now())
                .userId(SecurityContextHolder.getPrincipal().getId())
                .generatedNextStatusTask(false)
                .taskTime(LocalDateTime.now())
                .nextBusinessStatus(maintenaceVo.getStatus())
                .nextBusinessStatusName(maintenaceVo.getStatusName())
                .operationType(TodoTaskFlowOperationEnum.CHANGE)
                .build();

        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, build, String.valueOf(maintenaceVo.getId()));

        WorkTaskFlowCirculationMessage workBuild = WorkTaskFlowCirculationMessage.builder()
                .businessKey(String.valueOf(maintenaceVo.getId()))
                .businessStatus(MaintenaceTaskStatusEnum.BE_MAINTAINED.name())
                .handlerTime(LocalDateTime.now())
                .userId(SecurityContextHolder.getPrincipal().getId())
                .generatedNextStatusTask(false)
                .taskTime(LocalDateTime.now())
                .nextBusinessStatus(maintenaceVo.getStatus())
                .nextBusinessStatusName(maintenaceVo.getStatusName())
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .build();
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workBuild, String.valueOf(maintenaceVo.getId()));
    }

}
