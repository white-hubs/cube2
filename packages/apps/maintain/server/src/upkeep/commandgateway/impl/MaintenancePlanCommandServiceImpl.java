package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskDelMessage;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceEventExtClient;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ChangeRuleStateParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenancePlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceObjectCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanRuleCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceUpdatePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectProjectsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionDeviceMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionExpMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.GenerativeRulesEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObjectProjects;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleConditionExp;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceTasksService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceIotRuleConfigParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceIotRuleParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceIotRuleParam1;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceIotRulesParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenancePlanCommandServiceImpl extends ServiceImpl<MaintenancePlanMapper, MaintenancePlan> implements MaintenancePlanCommandService {

    private final MaintenancePlanMapper maintenancePlanMapper;

    private final MaintenanceObjectMapper maintenanceObjectMapper;

    private final MaintenanceObjectProjectsMapper maintenanceObjectProjectsMapper;

    private final MaintenanceTasksService maintenanceTasksService;

    private final UserClient userClient;
    private final MaintenanceRuleConditionMapper ruleConditionMapper;
    private final MaintenanceRuleConditionDeviceMapper ruleConditionDeviceMapper;
    private final MaintenanceRuleConditionExpMapper ruleConditionExpMapper;
    private final IotLinkageClient iotLinkageClient;
    private final IotDeviceEventExtClient iotDeviceEventExtClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlan(MaintenancePlanCommand param) {
        MaintenancePlan maintenancePlan1 = maintenancePlanMapper.selectOne(Wrappers.<MaintenancePlan>lambdaQuery()
                                                                                   .eq(MaintenancePlan::getMaintenancePlanName, param.getMaintenancePlanName()));
        Assert.isConflict(maintenancePlan1 != null, "计划名称重复");
        Long planId = IdWorker.getId();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        MaintenancePlan maintenancePlan = ObjectUtils.copying(param, MaintenancePlan.class);
        List<Long> orIds = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();
        if (!param.getReceiveDeptIds().isEmpty()) {
            String join = String.join("-", param.getReceiveDeptIds());
            maintenancePlan.setReceiveDeptId(join);
            orIds = ObjectUtils.copying(param.getReceiveDeptIds(), Long.class);
        }
        if (!param.getReceivePostIds().isEmpty()) {
            String join = String.join("-", param.getReceivePostIds());
            maintenancePlan.setReceivePostId(join);
            postIds = ObjectUtils.copying(param.getReceivePostIds(), Long.class);
        }
        if (!param.getReceiveIds().isEmpty()) {
            String join = String.join("-", param.getReceiveIds());
            maintenancePlan.setResponsibilityPersonId(join);
        }

        if (!orIds.isEmpty() || !postIds.isEmpty()) {
            List<Long> longs = userClient.listEnableUserByOrgAndPost(orIds, postIds);
            Assert.isNotAcceptable(longs.isEmpty(), "责任人检测为空");
            List<UserDTO> userDTOS = userClient.listUserByIds(longs);
            if (CollectionUtils.isNotEmpty(userDTOS)) {
                String userId = userDTOS.stream().map(t -> {
                    return String.valueOf(t.getId());
                }).collect(Collectors.joining("-"));
                maintenancePlan.setResponsibilityPersonId(userId);
            }
        }

        maintenancePlan.setTenant(tenant);
        maintenancePlan.setId(planId);
        if (GenerativeRulesEnum.JOB.name().equals(param.getWay())) {
            //保存首次任务生成时间
            var planStartTime = param.getPlanStartTime();
            var date = LocalDateTime.now();
            String yyyyMMdd = DateUtils.format(date, "yyyyMMdd");
            String format1 = DateUtils.format(planStartTime, "yyyyMMdd");
            String nextTaskTime = "";
            if (param.getMaintenanceTimePoint() < 10) {
                if (yyyyMMdd.equals(format1)) {
                    int hour = date.getHour();
                    if (param.getMaintenanceTimePoint() > hour) {
                        nextTaskTime = format1 + "0" + param.getMaintenanceTimePoint();
                    } else {
                        var offset = DateUtils.offset(planStartTime, ChronoUnit.DAYS, 1);
                        String yyyyMMdd1 = DateUtils.format(offset, "yyyyMMdd");
                        nextTaskTime = yyyyMMdd1 + "0" + param.getMaintenanceTimePoint();
                    }
                } else {
                    nextTaskTime = format1 + "0" + param.getMaintenanceTimePoint();
                }

            } else {
                if (yyyyMMdd.equals(format1)) {
                    int hour = date.getHour();
                    if (param.getMaintenanceTimePoint() > hour) {
                        nextTaskTime = format1 + param.getMaintenanceTimePoint();
                    } else {
                        var offset = DateUtils.offset(planStartTime, ChronoUnit.DAYS, 1);
                        String yyyyMMdd1 = DateUtils.format(offset, "yyyyMMdd");
                        nextTaskTime = yyyyMMdd1 + param.getMaintenanceTimePoint();
                    }
                } else {
                    nextTaskTime = format1 + param.getMaintenanceTimePoint();
                }
            }
            maintenancePlan.setNextTaskTime(nextTaskTime);
        }
        List<MaintenanceObjectCommand> maintenanceObjectCommands = param.getMaintenanceObjectCommands();
        if (!maintenanceObjectCommands.isEmpty()) {
            //保存维保对象及维保项目
            maintenanceObjectCommands.forEach(maintenanceObjectCommand -> {
                long objectId = IdWorker.getId();
                MaintenanceObject maintenanceObject = ObjectUtils.copying(maintenanceObjectCommand, MaintenanceObject.class);
                maintenanceObject.setId(objectId);
                maintenanceObject.setTenant(tenant);
                maintenanceObject.setPlanId(planId);
                List<String> projectIds = maintenanceObjectCommand.getProjectIds();
                Assert.isNotAcceptable(projectIds.isEmpty(), "没有维保项");
                projectIds.forEach(s -> {
                    long objectProjectsId = IdWorker.getId();
                    MaintenanceObjectProjects maintenanceObjectProjects = new MaintenanceObjectProjects();
                    maintenanceObjectProjects.setId(objectProjectsId);
                    maintenanceObjectProjects.setTenant(tenant);
                    maintenanceObjectProjects.setObjectId(objectId);
                    maintenanceObjectProjects.setProjectId(Long.valueOf(s));
                    maintenanceObjectProjectsMapper.insert(maintenanceObjectProjects);
                });
                maintenanceObjectMapper.insert(maintenanceObject);
            });
            maintenancePlanMapper.insert(maintenancePlan);
        }
        // 下放规则到iot
        if (GenerativeRulesEnum.RULE.name().equals(param.getWay())) {
            this.saveRlue(param, planId, MaintenacePlanStatusEnum.ENABLED.equals(param.getStatus()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(MaintenanceUpdatePlanCommand param) {
        MaintenancePlan maintenancePlan = maintenancePlanMapper.selectById(param.getId());
        maintenancePlan.setStatus(param.getStatus());
        this.updateById(maintenancePlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        List<MaintenanceTasks> list = maintenanceTasksService.list(Wrappers.<MaintenanceTasks>lambdaQuery()
                                                                           .eq(MaintenanceTasks::getPlanId, id)
                                                                           .eq(MaintenanceTasks::getMaintenanceTaskStatus, MaintenaceTaskStatusEnum.BE_MAINTAINED));
        Assert.isNotAcceptable(!list.isEmpty(), "有任务未完成不能删除");
        DatabaseContextHolder.softDelete(this, id);
        list.forEach(s -> {
            DatabaseContextHolder.softDelete(maintenanceTasksService, s.getId());
            //代办删除
            TodoTaskDelMessage todoTaskDelMessage = new TodoTaskDelMessage();
            todoTaskDelMessage.setBusinessKey(s.getId().toString());
            todoTaskDelMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
            RocketMQContextHolder.send(TodoTaskFlowMessage.TOPIC, todoTaskDelMessage);

            //工作中心删除
            WorkTaskDelMessage workTaskDelMessage = new WorkTaskDelMessage();
            workTaskDelMessage.setBusinessKey(s.getId().toString());
            workTaskDelMessage.setOperationType(WorkTaskFlowOperationEnum.DELETE);
            RocketMQContextHolder.send(WorkTaskDelMessage.TOPIC, workTaskDelMessage);
        });
    }

    /**
     * 插入触发规则
     */
    private void saveRlue(MaintenancePlanCommand param, Long planId, Boolean bool) {
        // 删除触发规则
        this.ruleConditionMapper.delete(Wrappers.<MaintenanceRuleCondition>lambdaQuery()
                                                .eq(MaintenanceRuleCondition::getEmergencyRuleId, planId));
        this.ruleConditionExpMapper.delete(Wrappers.<MaintenanceRuleConditionExp>lambdaQuery()
                                                   .eq(MaintenanceRuleConditionExp::getEmergencyRuleId, planId));
        for (MaintenancePlanRuleCommand ex : param.getRuleDetails()) {
            MaintenanceRuleCondition ruleCondition = ConvertUtils.convert(MaintenanceRuleCondition.class, ex);
            ruleCondition.setId(IdWorker.getId());
            ruleCondition.setEmergencyRuleId(planId);
            this.ruleConditionMapper.insert(ruleCondition);
            List<MaintenanceRuleConditionExp> ruleConditionExpList = ConvertUtils.toList(MaintenanceRuleConditionExp.class, ex.getEmergencyRuleConditionExps());
            ruleConditionExpList.forEach(exp -> {
                exp.setEmergencyRuleConditionId(ruleCondition.getId().toString());
                this.ruleConditionExpMapper.insert(exp);
            });
            if (StringUtils.equalsAny(ex.getConditionType(), "DEVICE_EVENT_CONDITION", "CUSTOM_RULE_CONDITION")) {
                this.saveLinkage(ruleCondition, ruleConditionExpList, bool, param.getMaintenanceObjectCommands());
            }
        }
    }

    /**
     * 下发规则到联动
     */
    private void saveLinkage(MaintenanceRuleCondition ruleCondition, List<MaintenanceRuleConditionExp> ruleConditionExpList, Boolean bool,
                             List<MaintenanceObjectCommand> maintenanceObjectCommands
                            ) {
        //iot获取规则
        RuleDetailDTO ruleDetailDTO = this.iotLinkageClient.ruleDetail(ruleCondition.getId().toString());
        if (Objects.isNull(ruleDetailDTO)) {
            // 规则下发到IOT
            MaintenanceIotRuleConfigParam configParam = new MaintenanceIotRuleConfigParam();
            configParam.setAction(new ArrayList<>());
            MaintenanceIotRuleParam maintenanceIotRuleParam = new MaintenanceIotRuleParam();
            maintenanceIotRuleParam.setOp("or");
            MaintenanceIotRuleParam1 iotRuleParam1 = new MaintenanceIotRuleParam1();
            iotRuleParam1.setOp("and");
            List<MaintenanceIotRulesParam> iotRulesParamList = new ArrayList<>();
            ruleConditionExpList.stream().forEach(ex -> {
                for (MaintenanceObjectCommand maintenanceObjectCommand : maintenanceObjectCommands) {
                    MaintenanceIotRulesParam iotRulesParam = new MaintenanceIotRulesParam();
                    iotRulesParam.setOp(ex.getOp());
                    if (StringUtils.isNotBlank(ex.getPropCode())) {
                        iotRulesParam.setPropCode(ex.getPropCode());
                    }
                    iotRulesParam.setDeviceId(maintenanceObjectCommand.getKeyId());
                    iotRulesParam.setDelayTime(ex.getDelayTime());
                    iotRulesParam.setOprand(Collections.singletonList(ex.getOprand()));
                    iotRulesParamList.add(iotRulesParam);
                }
            });
            iotRuleParam1.setRules(iotRulesParamList);
            maintenanceIotRuleParam.setRules(Collections.singletonList(iotRuleParam1));
            configParam.setRule(maintenanceIotRuleParam);
            RuleCreateParam ruleCreateParam = new RuleCreateParam();
            ruleCreateParam.setRuleName("维保作业");
            ruleCreateParam.setRuleNum(String.valueOf(ruleCondition.getId()));
            ruleCreateParam.setRuleConfig(JSONObject.parseObject(JSON.toJSONString(configParam)));
            ruleCreateParam.setEnabled("true");
            this.iotLinkageClient.ruleAdd(ruleCreateParam);
        } else {
            //设置规则为禁用
            ChangeRuleStateParam changeRuleStateParam = new ChangeRuleStateParam();
            changeRuleStateParam.setRuleNums(Collections.singletonList(ruleCondition.getId().toString()));
            //设置规则为状态
            changeRuleStateParam.setEnabled(bool);
            this.iotLinkageClient.changeRuleStateBatch(changeRuleStateParam);
        }
    }
}
