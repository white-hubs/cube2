package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskCirculateConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlan;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlanTaskConfig;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionPlanStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskGenCirculateTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionNotifyHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanTaskConfigService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionPlanJob {
    private final static Integer TIME_DURATION = 30;
    private final static Integer RECENT_CREATE_TIME_DURATION = 3600;
    private final static String DISINFECTION_PLAN_REDIS_LOCK = "disinfection:plan:";
    private final DisinfectionPlanService disinfectionPlanService;
    private final DisinfectionPlanTaskConfigService planTaskConfigService;
    private final DisinfectionTaskService taskService;
    private final DisinfectionNotifyHelper disinfectionNotifyHelper;
    private final DisinfectionDataTypeService disinfectionDataTypeService;

    private static List<DisinfectionPlan> getExecutePlans(List<DisinfectionPlan> plans) {
        return plans.stream()
                .filter(plan -> DisinfectionPlanStatusEnum.EFFECTIVE.equals(plan.getStatus()))
                .filter(plan -> DisinfectionEnableEnum.ENABLE.equals(plan.getEnable()))
                .collect(Collectors.toList());
    }

    private static boolean isInConfigTimePeriod(LocalDateTime now, LocalDateTime configTime) {
        //获取配置时间3秒前时间
        LocalDateTime threeSecondsBefore = configTime.minusSeconds(TIME_DURATION);
        //获取配置时间3秒后时间
        LocalDateTime threeSecondsAfter = configTime.plusSeconds(TIME_DURATION);
        return now.isAfter(threeSecondsBefore) && now.isBefore(threeSecondsAfter);
    }

    private static PlanTaskTimeLimitConfigDTO getPlanTaskTimeLimitConfigDTO(DisinfectionPlan plan) {
        String taskTimeLimitConfig = plan.getTaskTimeLimitConfig();
        return StringUtils.isBlank(taskTimeLimitConfig) ? null
                                                        : JsonUtils.deserialize(taskTimeLimitConfig, PlanTaskTimeLimitConfigDTO.class);
    }

    private static boolean isRecentHasExecute(PlanTaskCirculateConfigDTO circulateConfigDTO, LocalDateTime now) {
        if (StringUtils.isBlank(circulateConfigDTO.getLastTime())) {
            return false;
        }
        //获取最近执行时间,判断是否最近执行过
        LocalDateTime lastTime = LocalDateTime.parse(circulateConfigDTO.getLastTime());
        return now.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() -
               lastTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() <= RECENT_CREATE_TIME_DURATION;
    }

    /**
     * 获取当前月份中，指定星期几对应的所有“日”列表（如 10, 17, 24）
     *
     * @param dayOfWeek 周几（1=周一，2=周二，...，7=周日）
     *
     * @return 当前月份中所有匹配的“日”组成的列表
     */
    public static List<Integer> getDaysByDayOfWeekInCurrentMonth(LocalDateTime now, int dayOfWeek) {
        List<Integer> result = new ArrayList<>();

        // 获取本月第一天
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1);
        // 从第一个匹配的日期开始，每隔7天添加一次，直到超过本月
        LocalDateTime current = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
        while (current.getMonthValue() == now.getMonthValue()) {
            result.add(current.getDayOfMonth());
            current = current.plusWeeks(1);
        }

        return result;
    }

    //固定速率执行（每隔5秒执行一次）
    @JobAnnotation(name = "disinfectionPlanExecute", cron = "0/30 * * * * ?", desc = "病媒生物-定时执行消杀计划")
    public void taskWithFixedRate(String param) {
        //查询当前存在的计划所关联的租户
        var allTenants = disinfectionPlanService.listAllTenantCodes();
        if (ListUtils.isEmpty(allTenants)) {
            return;
        }
        allTenants.forEach(tenant -> {
            log.debug("租户:{},消杀计划定时任务开始执行=============================>", tenant);
            var token = RedisUtils.tryLock(DISINFECTION_PLAN_REDIS_LOCK + tenant.toString(), Expiration.seconds(10));
            try {
                if (StringUtils.isBlank(token)) {
                    return;
                }
                SessionContextHolder.requestTenant(tenant);
                var plans = disinfectionPlanService.list();
                if (ListUtils.nonEmpty(plans)) {
                    //更新所有计划状态
                    batchUpdatePlanStatus(plans);
                    //检测计划任务创建
                    batchCreatePlanTask(plans);
                }
                SessionContextHolder.clearSessionContext();
            } catch (Exception e) {
                log.error("租户:{}定时任务，计划处理发生错误:{}", tenant, e.getMessage());
            } finally {
                if (StringUtils.isNotBlank(token)) {
                    RedisUtils.unlock(DISINFECTION_PLAN_REDIS_LOCK + tenant, token);
                }
            }
            log.debug("租户:{},消杀计划定时任务执行结束=============================>", tenant);

        });

    }

    private LocalDateTime getConfigDateTime(DisinfectionTaskGenCirculateTypeEnum type, PlanTaskCirculateConfigDTO circulateConfigDTO,
                                            LocalDateTime now
                                           ) {
        //获取当前年份
        int year = now.getYear();
        //获取当前月份
        String monthStr = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
        //获取当前第几周
        int week = now.get(WeekFields.ISO.weekOfMonth());
        //获取当前第几日
        String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : String.valueOf(now.getDayOfMonth());
        Integer configDTODay = circulateConfigDTO.getDay();
        String dateStr = "";
        if (DisinfectionTaskGenCirculateTypeEnum.MONTHLY.equals(type)) {
            String configDayStr =
                    configDTODay < 10 ? "0" + configDTODay : String.valueOf(configDTODay);
            dateStr = year + "-" + monthStr + "-" + configDayStr + "T" + circulateConfigDTO.getTime();

        }
        if (DisinfectionTaskGenCirculateTypeEnum.WEEKLY.equals(type)) {
            //获取当前月所有的对应用户配置的日期
            List<Integer> dayList = getDaysByDayOfWeekInCurrentMonth(now, configDTODay);

            int configDay = dayList.get(dayList.size() - 1);
            //判断当天是否属于生效范围内，如果不属于则返回最后一个生效日期
            if (dayList.contains(Integer.valueOf(day))) {
                configDay = Integer.parseInt(day);
            }
            String configDayStr =
                    configDay < 10 ? "0" + configDay : String.valueOf(configDay);
            dateStr = year + "-" + monthStr + "-" + configDayStr + "T" + circulateConfigDTO.getTime();
        }
        if (DisinfectionTaskGenCirculateTypeEnum.DAILY.equals(type)) {
            dateStr = year + "-" + monthStr + "-" + day + "T" + circulateConfigDTO.getTime();
        }
        Assert.isParameterError(StringUtils.isEmpty(dateStr), "计划任务配置时间格式错误");
        return LocalDateTime.parse(dateStr);
    }

    private void weeklyHandle(PlanTaskCirculateConfigDTO circulateConfigDTO, DisinfectionPlan plan,
                              List<DisinfectionPlanTaskConfig> planTaskConfigs
                             ) {
        LocalDateTime now = LocalDateTime.now();
        //获取配置的执行的时间
        LocalDateTime configTime = getConfigDateTime(DisinfectionTaskGenCirculateTypeEnum.WEEKLY, circulateConfigDTO, now);
        //获取最近执行时间
        //判断当前时间是否在配置时间3秒内
        if (isInConfigTimePeriod(now, configTime)) {
            //判断任务最近是否有执行，如果有执行还需要判断上次执行时间是否离当前时间大于6秒，如果小于6秒则不生成任务
            if (isRecentHasExecute(circulateConfigDTO, now)) {
                return;
            }
            //符合生成条件，需要生成任务，并创建生成的任务记录
            creatTask(planTaskConfigs, plan);
            //更新配置最后执行时间
            circulateConfigDTO.setLastTime(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    private void dailyHandle(PlanTaskCirculateConfigDTO circulateConfigDTO, DisinfectionPlan plan,
                             List<DisinfectionPlanTaskConfig> planTaskConfigs
                            ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime configTime = getConfigDateTime(DisinfectionTaskGenCirculateTypeEnum.DAILY, circulateConfigDTO, now);

        //判断当前时间是否在配置时间3秒内
        if (isInConfigTimePeriod(now, configTime)) {
            //判断任务最近是否有执行，如果有执行还需要判断上次执行时间是否离当前时间大于6秒，如果小于6秒则不生成任务
            if (isRecentHasExecute(circulateConfigDTO, now)) {
                return;
            }
            //符合生成条件，需要生成任务，并创建生成的任务记录
            creatTask(planTaskConfigs, plan);
            //更新配置最后执行时间
            circulateConfigDTO.setLastTime(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    private void batchCreatePlanTask(@NonNull List<DisinfectionPlan> plans) {
        plans = plans.stream()
                .filter(plan -> DisinfectionEnableEnum.ENABLE.equals(plan.getEnable()))
                .filter(plan -> DisinfectionPlanStatusEnum.EFFECTIVE.equals(plan.getStatus()))
                .collect(Collectors.toList());
        List<DisinfectionPlan> executePlans = getExecutePlans(plans);
        //获取计划的任务配置
        List<DisinfectionPlanTaskConfig> taskConfigs = planTaskConfigService.getPlanTaskByPlanIds(ListUtils.convertItem(executePlans, DisinfectionPlan::getId));
        if (ListUtils.isEmpty(taskConfigs)) {
            return;
        }
        Map<Long, List<DisinfectionPlanTaskConfig>> planConfigTaskMap = MapUtils.listGroupToMap(taskConfigs, DisinfectionPlanTaskConfig::getPlanId);
        plans.forEach(plan -> {
            List<DisinfectionPlanTaskConfig> planTaskConfigs = planConfigTaskMap.getOrDefault(plan.getId(), Collections.emptyList());
            if (ListUtils.isEmpty(planTaskConfigs)) {
                return;
            }
            String taskGenerationCycleConfig = plan.getTaskGenerationCycleConfig();
            if (StringUtils.isBlank(taskGenerationCycleConfig)) {
                return;
            }
            List<PlanTaskCirculateConfigDTO> circulateConfigDTOs = JsonUtils.deserialize(taskGenerationCycleConfig, new TypeReference<List<PlanTaskCirculateConfigDTO>>() {});
            if (ListUtils.isEmpty(circulateConfigDTOs)) {
                return;
            }
            circulateConfigDTOs.forEach(circulateConfigDTO -> {
                //判断是否生成任务
                if (DisinfectionTaskGenCirculateTypeEnum.MONTHLY.equals(circulateConfigDTO.getType())) {
                    monthlyHandle(circulateConfigDTO, plan, planTaskConfigs);
                } else if (DisinfectionTaskGenCirculateTypeEnum.WEEKLY.equals(circulateConfigDTO.getType())) {
                    weeklyHandle(circulateConfigDTO, plan, planTaskConfigs);
                } else if (DisinfectionTaskGenCirculateTypeEnum.DAILY.equals(circulateConfigDTO.getType())) {
                    dailyHandle(circulateConfigDTO, plan, planTaskConfigs);
                } else {
                    log.error("不支持的生成周期类型");
                }
            });

            plan.setTaskGenerationCycleConfig(JsonUtils.serialize(circulateConfigDTOs));
        });
        disinfectionPlanService.updateBatchById(plans);

    }

    private void monthlyHandle(PlanTaskCirculateConfigDTO circulateConfigDTO, DisinfectionPlan plan,
                               List<DisinfectionPlanTaskConfig> planTaskConfigs
                              ) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime configTime = getConfigDateTime(DisinfectionTaskGenCirculateTypeEnum.MONTHLY, circulateConfigDTO, now);

        //判断当前时间是否在配置时间3秒内
        if (isInConfigTimePeriod(now, configTime)) {
            //判断任务最近是否有执行，如果有执行还需要判断上次执行时间是否离当前时间大于6秒，如果小于6秒则不生成任务
            if (isRecentHasExecute(circulateConfigDTO, now)) {
                return;
            }
            //符合生成条件，需要生成任务，并创建生成的任务记录
            creatTask(planTaskConfigs, plan);
            //更新配置最后执行时间
            circulateConfigDTO.setLastTime(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    private void batchUpdatePlanStatus(@NonNull List<DisinfectionPlan> plans) {

        LocalDateTime now = LocalDateTime.now();
        plans.forEach(plan -> {
            LocalDateTime beginTime = plan.getBeginTime();
            LocalDateTime endTime = plan.getEndTime();
            if (now.isBefore(beginTime)) {
                plan.setStatus(DisinfectionPlanStatusEnum.NOT_STARTED);
            }
            if (now.isAfter(beginTime) && now.isBefore(endTime)) {
                plan.setStatus(DisinfectionPlanStatusEnum.EFFECTIVE);
            }
            if (now.isAfter(endTime)) {
                plan.setStatus(DisinfectionPlanStatusEnum.ENDED);
            }
        });
        disinfectionPlanService.updateBatchById(plans);
    }

    private void creatTask(@NonNull List<DisinfectionPlanTaskConfig> planTaskConfigs, DisinfectionPlan plan) {
        if (ListUtils.isEmpty(planTaskConfigs)) {
            return;
        }
        PlanTaskTimeLimitConfigDTO timeLimitConfigDTO = getPlanTaskTimeLimitConfigDTO(plan);

        List<DisinfectionTask> taskList = planTaskConfigs.stream().map(planTaskConfig -> {
            LocalDateTime now = LocalDateTime.now();
            planTaskConfig.setGenerateCount(planTaskConfig.getGenerateCount() + 1);
            DisinfectionTask disinfectionTask = ObjectUtils.copying(planTaskConfig, DisinfectionTask.class);
            disinfectionTask.setId(IdWorker.getId());
            disinfectionTask.setTaskName(
                    plan.getPlanName() + "_" + planTaskConfig.getTaskName() + "_" + getTaskNameCount(planTaskConfig));
            disinfectionTask.setStatus(DisinfectionTaskStatusEnum.WAIT_RECEIVE);
            disinfectionTask.setTaskTimeLimitConfig(plan.getTaskTimeLimitConfig());
            disinfectionTask.setPlanId(plan.getId());
            disinfectionTask.setCreateTime(now);
            disinfectionTask.setUpdateTime(now);
            //设置项目负责人
            disinfectionTask.setProjectLeaderId(plan.getProjectLeaderId());
            //设置区域名称
            disinfectionTask.setTaskTypeName(disinfectionDataTypeService.getTypeName(disinfectionTask.getTaskTypeId()));
            if (Objects.nonNull(timeLimitConfigDTO)) {

                //设置截止时间
                Integer day = timeLimitConfigDTO.getDay();
                if (Objects.nonNull(day)) {
                    now = now.plusDays(day);
                }
                Integer hour = timeLimitConfigDTO.getHour();
                if (Objects.nonNull(hour)) {
                    now = now.plusHours(hour);
                }
                Integer minute = timeLimitConfigDTO.getMinute();
                if (Objects.nonNull(minute)) {
                    now = now.plusMinutes(minute);
                }
                if (Objects.nonNull(day) || Objects.nonNull(hour) || Objects.nonNull(minute)) {
                    //设置截至日期
                    disinfectionTask.setDeadline(now);
                }
            }
            disinfectionNotifyHelper.sendDisinfectionTaskCreateMessage(disinfectionTask);
            log.info("计划:{},任务创建完成，任务信息:{}", planTaskConfig.getPlanId(), disinfectionTask);
            return disinfectionTask;
        }).collect(Collectors.toList());
        taskService.saveBatch(taskList);
        planTaskConfigService.updateBatchById(planTaskConfigs);

    }

    private static String getTaskNameCount(DisinfectionPlanTaskConfig planTaskConfig) {
        int generateCount = planTaskConfig.getGenerateCount();
        String str = "V";
        if (generateCount < 10) {
            str += "000";
        } else if (generateCount < 100) {
            str += "00";
        } else if (generateCount < 1000) {
            str += "0";
        }
        return str + generateCount;
    }
}
