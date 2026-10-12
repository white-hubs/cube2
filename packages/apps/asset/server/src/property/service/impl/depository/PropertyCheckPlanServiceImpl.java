package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.property.management.client.property.enumeration.MessageTypeEnum;
import com.guhecloud.cube.property.management.client.property.event.message.PropertyMessage;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskDataSourceParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckPlanPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskEvolveVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedExcelVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckPlanMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckPlan;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTaskRelation;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.CheckTaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertySourceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckPlanService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckTaskService;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskCenterUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskTodoUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 盘点计划服务实现类
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyCheckPlanServiceImpl extends ServiceImpl<PropertyCheckPlanMapper, PropertyCheckPlan> implements PropertyCheckPlanService {
    private final PropertyCheckTaskService taskService;
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final UserClientService userClientService;
    private final PropertyCheckTaskMapper checkTaskMapper;
    private final PropertyCheckTaskRelationMapper taskRelationMapper;
    private final AreaClient areaClient;
    private final PropertyTaskTodoUtil propertyTaskTodoUtil;
    private final PropertyTaskCenterUtil propertyTaskCenterUtil;

    /**
     * 通过ID查询盘点计划详情
     *
     * @param id
     *
     * @return 盘点计划详情
     */
    public PropertyCheckTaskVO queryById(Long id) {
        PropertyCheckPlan plan = this.baseMapper.selectById(id);
        PropertyCheckTaskVO checkTask = ConvertUtils.convert(PropertyCheckTaskVO.class, plan);
        if (ObjectUtils.isNotEmpty(plan.getDataSource())) {
            checkTask.setTaskDataSource(JsonUtils.deserialize(plan.getDataSource(), PropertyCheckTaskDataSourceParam.class));
            List<String> areaIds = checkTask.getTaskDataSource().getAreaIds();
            if (ObjectUtils.isNotEmpty(areaIds)) {
                List<AreaDTO> area = areaClient.getByIds(areaIds.stream().map(Long::parseLong).collect(Collectors.toList()));
                checkTask.getTaskDataSource().setArea(area);
            }
        } else {
            checkTask.setTaskDataSource(new PropertyCheckTaskDataSourceParam());
        }
        checkTask.setDutyUserId(userClientService.getUser(checkTask.getDutyUserId()));
        return checkTask;
    }

    /**
     * 查询盘点计划统计
     *
     * @param id
     *
     * @return 查询盘点计划统计
     */
    @Override
    public PropertyCheckTaskEvolveVO queryByPalan(Long id) {
        PropertyCheckTaskEvolveVO plan = checkTaskMapper.queryByPalan(id);
        if (ObjectUtils.isEmpty(plan.getCheckCompleteNumber())) {
            plan.setCheckCompleteNumber(0);
        }
        if (ObjectUtils.isEmpty(plan.getCheckLossNumber())) {
            plan.setCheckLossNumber(0);
        }
        plan.setCheckUndiskNumber(
                plan.getCheckTotalNumber() - plan.getCheckCompleteNumber() - plan.getCheckLossNumber() - plan.getAbnormalNumber());
        Long abnormal = taskRelationMapper.selectCount(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                               .eq(PropertyCheckTaskRelation::getPlanId, id)
                                                               .isNotNull(PropertyCheckTaskRelation::getAbnormalType));
        plan.setAbnormalNumber(Optional.ofNullable(abnormal).orElse(0L));
        return plan;
    }

    /**
     * 分页查询盘点计划
     *
     * @param param 筛选条件
     *
     * @return 盘点计划信息
     */
    public Pagination<PropertyCheckPlanPageVO> getPropertyCheckPlanPage(PropertyCheckPlanPageParam param) {
        Page<PropertyCheckPlan> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<PropertyCheckPlan>()
                .eq(StringUtils.isNotEmpty(param.getStatus()), PropertyCheckPlan::getStatus, param.getStatus())
                .like(StringUtils.isNotEmpty(param.getTaskName()), PropertyCheckPlan::getTaskName, param.getTaskName())
                .orderByDesc(PropertyCheckPlan::getCreateTime));
        Pagination<PropertyCheckPlanPageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(),
                                                                          ConvertUtils.toList(PropertyCheckPlanPageVO.class, page.getRecords()));
        for (PropertyCheckPlanPageVO ex : pagination.getRecords()) {
            PropertyCheckTaskEvolveVO taskEvolve = checkTaskMapper.getCheckTaskEvolve(ex.getId());
            ex.setCheckCompleteNumber(taskEvolve.getCheckCompleteNumber());
            ex.setCheckLossNumber(taskEvolve.getCheckLossNumber());
            ex.setCheckTotalNumber(taskEvolve.getCheckTotalNumber());
            ex.setCheckWinNumber(taskEvolve.getCheckWinNumber());
            ex.setCheckUndiskNumber(ex.getPropertyNumber() - ex.getCheckCompleteNumber());
            ex.setDutyUserName(userClientService.getUser(ex.getDutyUserId()));
        }
        return pagination;
    }

    /**
     * 新增盘点计划
     *
     * @param param 盘点计划数据
     *
     * @return 新增盘点计划结果
     */
    public Boolean insert(PropertyCheckPlanParam param) {
        log.info("add：资产盘点计划");
        PropertyCheckPlan plan = ConvertUtils.convert(PropertyCheckPlan.class, param);
        plan.setStatus(TaskStatusEnum.UNDONE.name());
        String areaId =
                ObjectUtils.isNotEmpty(param.getTaskDataSource().getAreaIds()) ? String.join(",", param.getTaskDataSource().getAreaIds())
                                                                               : null;
        plan.setDataSource(JsonUtils.serialize(param.getTaskDataSource()));
        if (ObjectUtils.isNotEmpty(param.getTaskDataSource().getTypeId())) {
            param.getTaskDataSource().setTypeIds(StringUtils.splitAndIgnoreBlank(param.getTaskDataSource().getTypeId()));
        }
        List<PropertyDetailedPageVO> detailedList = propertyDetailedMapper.getPropertyDetailedList(param.getTaskDataSource(), areaId);
        Assert.isNotFound(ObjectUtils.isEmpty(detailedList), "没有可盘点资产");
        plan.setPropertyNumber(detailedList.size());
        Boolean bool = this.baseMapper.insert(plan) > 0;
        log.info("写入盘点计划:{}", bool);
        if (bool) {
            Integer number = taskService.insertList(plan, detailedList);
            // 更新资产数量
            this.lambdaUpdate().eq(PropertyCheckPlan::getId, plan.getId())
                    .set(PropertyCheckPlan::getPropertyNumber, number)
                    .update();
        }
        return bool;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        Boolean bool = DatabaseContextHolder.softDelete(this, id);
        // 发送删除资产消息
        RocketMQContextHolder.send(PropertyMessage.TOPIC, PropertyMessage.builder()
                .id(id)
                .status(MessageTypeEnum.DELETED.name())
                .build());
        return bool;
    }

    @Override
    public Boolean submit(Long planId, Long taskId) {
        if (ObjectUtils.isEmpty(planId) && ObjectUtils.isEmpty(taskId)) {
            throw new RuntimeException("不存在任务");
        }
        boolean bool = false;
        if (ObjectUtils.isNotEmpty(planId)) {
            Long plan = checkTaskMapper.selectCount(new LambdaQueryWrapper<PropertyCheckTask>()
                                                            .eq(PropertyCheckTask::getPlanId, planId)
                                                            .eq(PropertyCheckTask::getStatus, TaskStatusEnum.UNDONE.name()));
            if (plan > 0) {
                throw new ServiceException("请完成所有的资产盘点后再提交");
            }
            PropertyCheckPlan checkPlan = baseMapper.selectById(planId);
            checkPlan.setStatus(TaskStatusEnum.FINISH.name());
            checkPlan.setRealityEndTime(DateUtils.now());
            baseMapper.updateById(checkPlan);
            // 查询是否有盘赢
            List<PropertyCheckTask> checkTaskList = checkTaskMapper.selectList(new LambdaQueryWrapper<PropertyCheckTask>()
                                                                                       .eq(PropertyCheckTask::getPlanId, planId)
                                                                                       .gt(PropertyCheckTask::getCheckWinNumber, 0));
            return ObjectUtils.isNotEmpty(checkTaskList) && !checkTaskList.isEmpty();
        } else if (ObjectUtils.isNotEmpty(taskId)) {
            // 判断是否有未盘点的资产
            Long task = taskRelationMapper.selectCount(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                               .eq(PropertyCheckTaskRelation::getTaskId, taskId)
                                                               .eq(PropertyCheckTaskRelation::getCheckStatus, CheckTaskStatusEnum.INCOMPLETE.name()));
            if (task < 1) {
                PropertyCheckTask checkTask = checkTaskMapper.selectById(taskId);
                checkTask.setStatus(TaskStatusEnum.FINISH.name());
                checkTask.setRealityEndTime(DateUtils.now());
                bool = checkTaskMapper.updateById(checkTask) > 0;
                propertyTaskTodoUtil.todo(checkTask, "PROPERTY_TASK");
                propertyTaskCenterUtil.work(checkTask, "PROPERTY_TASK");
            }
        }
        return bool;
    }

    @Override
    public Boolean submitWin(Long planId, Long taskId) {
        List<PropertyCheckTaskRelation> taskRelationList = taskRelationMapper.selectList(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                                                 .eq(PropertyCheckTaskRelation::getPlanId, planId)
                                                                                                 .eq(PropertyCheckTaskRelation::getCheckStatus, CheckTaskStatusEnum.WIN.name()));
        taskRelationList.forEach(ex -> {
            PropertyDetailed detailed = propertyDetailedMapper.selectById(ex.getPropertyId());
            detailed.setStatus(ObjectUtils.isNotEmpty(detailed.getReceiveDepId()) ? PropertyStatusEnum.MAKE_USE_OF.name()
                                                                                  : PropertyStatusEnum.LEAVE_UNUSED.name());
            detailed.setAuthority(PropertySourceEnum.DEPOSITORY.name());
            propertyDetailedMapper.updateById(detailed);
            // 发送新增资产消息
            RocketMQContextHolder.send(PropertyMessage.TOPIC, PropertyMessage.builder()
                    .id(detailed.getId())
                    .status(MessageTypeEnum.ADD.name())
                    .build());
        });
        return true;
    }

    @Override
    public void getExcel(HttpServletResponse response, String ids) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] array = ids.split(",");
        for (String id : array) {
            PropertyCheckPlan plan = baseMapper.selectById(Long.parseLong(id));
            Map<String, String> map = new HashMap<>();
            PropertyCheckTaskEvolveVO task = this.queryByPalan(Long.parseLong(id));
            map.put("planName", plan.getTaskName());
            map.put("checkTotalNumber", String.valueOf(task.getCheckTotalNumber() + task.getCheckWinNumber()));
            map.put("checkCompleteNumber", task.getCheckCompleteNumber().toString());
            map.put("checkUndiskNumber", task.getCheckUndiskNumber().toString());
            map.put("checkWinNumber", task.getCheckWinNumber().toString());
            map.put("checkLossNumber", task.getCheckLossNumber().toString());
            map.put("abnormalNumber", task.getAbnormalNumber().toString());
            List<PropertyDetailedExcelVO> detailed = taskService.queryByPlanIdPage(id);
            for (PropertyDetailedExcelVO ex : detailed) {
                if (ObjectUtils.isNotEmpty(ex.getUsageDate())) {
                    // 计算剩余使用期限
                    long usageTime = DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1 ? 0
                                                                                                   : DateUtils.betweenMonth(LocalDateTime.now(), ex.getUsageDate(), true);
                    if (usageTime < 1) {
                        usageTime = 0L;
                    }
                    ex.setUsageTime(usageTime);
                    ex.setUsage(DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1 ? "是" : "否");
                    ex.setCheckTimeStr(DateUtils.format(ex.getCheckTime()));
                    ex.setStartTimeStr(DateUtils.format(ex.getStartTime()));
                }
            }
            title.put(id, map);
            data.put(id, detailed);
        }
        try {
            log.info("----------------------开始导出固定资产盘点----------------------");
            ExcelUtile.uploadExcelList(response, data, title, "固定资产盘点.xlsx");
        } catch (Exception exception) {
            log.error("固定资产盘点导出--{}", exception.getLocalizedMessage());
        }
    }
}
