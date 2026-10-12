package com.guhecloud.cube.srv.gen.maintain.server.common.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.FaultOrderStateEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.OmPostureService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultHandleStatusStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultObjectTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectRecordTrendVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectTaskTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.MaintenanceStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.TicketStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceTasksService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.PeriodTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * author: ht
 * create: 2025-06-04 14:51
 */
@Service
@RequiredArgsConstructor
public class OmPostureServiceImpl implements OmPostureService {
    private final FaultOrderService faultOrderService;
    private final InspectRecordCommandService recordCommandService;
    private final MaintenanceTasksService maintenanceTasksService;
    private final TaskService taskService;
    private final UserClient userClient;

    @Override
    public TicketStatisticsVo ticketStatistics(@NonNull Period period) {
        //查询保修故障单数量
        LambdaQueryChainWrapper<FaultOrder> queryChainWrapper = faultOrderService.lambdaQuery()
                .between(FaultOrder::getCreateTime, period.getBegin(), period.getEnd());
        long faultCount = queryChainWrapper.count();
        //查询维保作业单数量
        LambdaQueryChainWrapper<MaintenanceTasks> maintenanceTasksLambdaQueryChainWrapper = maintenanceTasksService.lambdaQuery()
                .between(MaintenanceTasks::getCreateTime, period.getBegin(), period.getEnd());
        long maintenanceCount = maintenanceTasksLambdaQueryChainWrapper.count();
        //查询日常巡检数量
        LambdaQueryChainWrapper<InspectTaskPO> taskLambdaQueryChainWrapper = taskService.lambdaQuery()
                .between(InspectTaskPO::getCreateTime, period.getBegin(), period.getEnd());
        long inspectCount = taskLambdaQueryChainWrapper.count();
        long total = faultCount + maintenanceCount + inspectCount;
        //封装返回数据
        BigDecimal maintenanceRate = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(maintenanceCount)
                .divide(BigDecimal.valueOf(faultCount + maintenanceCount + inspectCount), 2, RoundingMode.HALF_UP);
        BigDecimal inspectRate = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(inspectCount)
                .divide(BigDecimal.valueOf(faultCount + maintenanceCount + inspectCount), 2, RoundingMode.HALF_UP);
        BigDecimal faultRate = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(faultCount)
                .divide(BigDecimal.valueOf(faultCount + maintenanceCount + inspectCount), 2, RoundingMode.HALF_UP);
        return TicketStatisticsVo.builder()
                .total(total)
                .faultRate(faultRate)
                .faultNum(faultCount)
                .maintenanceRate(maintenanceRate)
                .maintenanceNum(maintenanceCount)
                .inspectRate(inspectRate)
                .inspectNum(inspectCount)
                .build();
    }

    @Override
    public List<FaultObjectTop5Vo> faultObjectTop5(@NonNull Period period) {
        //查询故障单数量
        LambdaQueryChainWrapper<FaultOrder> queryChainWrapper = faultOrderService.lambdaQuery()
                .between(FaultOrder::getSubmitTime, period.getBegin(), period.getEnd());
        List<FaultOrder> list = queryChainWrapper.list();
        list = ListUtils.removeIfNull(list, FaultOrder::getObjId);

        Map<Long, List<FaultOrder>> countMap = MapUtils.listGroupToMap(list, FaultOrder::getObjId);
        return countMap
                .values()
                .stream()
                .map(value -> {
                    //分组来的，一定有一个元素
                    FaultOrder order = value.get(0);
                    return FaultObjectTop5Vo.builder().faultNum((long) value.size()).faultObject(order.getObjName()).build();
                })
                //降序排序
                .sorted((o1, o2) -> o2.getFaultNum().compareTo(o1.getFaultNum()))
                .limit(5)
                .collect(Collectors.toList());

    }

    @Override
    public List<FaultHandleStatusStatisticsVo> faultHandleStatusStatistics(@NonNull Period period) {
        //获取时间段类的的故障报修信息
        List<FaultOrder> faultOrders = faultOrderService.lambdaQuery()
                .between(FaultOrder::getSubmitTime, period.getBegin(), period.getEnd()).list();
        //根据故障报修信息按照故障报修单状态分组
        Map<FaultOrderStateEnum, List<FaultOrder>> stateMap = MapUtils.listGroupToMap(faultOrders, faultOrder -> {
            String state = faultOrder.getState();
            return FaultOrderStateEnum.getByValue(state);
        });
        long total = stateMap.values().stream().map(List::size).reduce(0, Integer::sum);
        return stateMap.entrySet().stream().map(entry -> {
            long count = entry.getValue().size();
            return FaultHandleStatusStatisticsVo.builder()
                    .statusType(entry.getKey().getValue())
                    .num(count)
                    .rate(BigDecimal.valueOf(count).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP))
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceStatisticsVo> maintenanceStatistics(@NonNull Period period, @NonNull PeriodTypeEnum periodTypeEnum) {
        // 查询发布时间在时间段内的维保任务
        List<MaintenanceTasks> tasks = maintenanceTasksService.lambdaQuery()
                .between(MaintenanceTasks::getMaintenanceTime, period.getBegin(), period.getEnd())
                .list();
        switch (periodTypeEnum) {
            case THIS_WEEK:
                return this.countMaintenanceTaskCountByDate(tasks, (date) -> String.valueOf(date.getDayOfWeek()));
            case THIS_MONTH:
                return this.countMaintenanceTaskCountByDate(tasks, this::dayOfMonth);
            case THIS_YEAR:
                return this.countMaintenanceTaskCountByDate(tasks, this::monthOfYear);
            default:
                return new ArrayList<>();
        }
    }

    private List<MaintenanceStatisticsVo> countMaintenanceTaskCountByDate(@NonNull List<MaintenanceTasks> tasks,
                                                                          @NonNull Function<LocalDateTime, String> dateConverter
                                                                         ) {
        // 按天分组统计，显示周一-周日
        return tasks.stream()
                .map(MaintenanceTasks::getMaintenanceTime)
                .map(DateUtils::parse)
                //转换为一周不同天数的数量
                .collect(Collectors.groupingBy(dateConverter, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> MaintenanceStatisticsVo.builder().date(String.valueOf(entry.getKey())).num(entry.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectTaskTop5Vo> patrolTaskTop5(@NonNull Period period) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        //人员巡检任务总数统计Map
        Map<String, Long> totalMap = new HashMap<>();
        //人员巡检任务完成数统计Map
        Map<String, Long> completeMap = new HashMap<>();
        //查询时间段内的巡检任务
        List<InspectTaskPO> tasks = taskService.lambdaQuery().between(InspectTaskPO::getBeginTime, period.getBegin(), period.getEnd())
                .ne(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT).list();
        if (CollectionUtils.isEmpty(tasks)) {
            return CollectionUtils.emptyList();
        }
        //因为每个任务执行人可能有多个，所以得把每个人都分出来进行统计
        tasks.stream()
                .filter(task -> Objects.nonNull(task.getReceiveIds()))
                //遍历每个巡检任务，给每个人统计巡检任务数以及任务完成数
                .forEach(task -> statisticTotalAndComplete(task, totalMap, completeMap));
        SessionContextHolder.requestTenant(tenant);
        List<Long> userIds = totalMap.keySet().stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> userDTOS = userClient.listUserByIds(userIds);
        Map<Long, String> idToNameMap = MapUtils.listToMap(userDTOS, UserDTO::getId, UserDTO::getName);
        return totalMap.entrySet().stream()
                .map(entry -> {
                    String userId = entry.getKey();
                    Long total = entry.getValue();
                    Long complete = completeMap.getOrDefault(userId, 0L);
                    return InspectTaskTop5Vo.builder().total(total).finishNum(complete)
                            .name(idToNameMap.getOrDefault(Long.valueOf(userId), "")).build();
                })
                //根据总数降序排序
                .sorted((o1, o2) -> o2.getTotal().compareTo(o1.getTotal()))
                //取前五
                .limit(5)
                .collect(Collectors.toList());
    }

    private static void statisticTotalAndComplete(InspectTaskPO task, Map<String, Long> totalMap, Map<String, Long> completeMap) {
        String[] receiveIds = task.getReceiveIds().split(",");
        InspectTaskStatusEnum status = task.getStatus();
        for (String receiveId : receiveIds) {
            Long total = totalMap.getOrDefault(receiveId, 0L);
            totalMap.put(receiveId, total + 1);
            if (InspectTaskStatusEnum.OVER.equals(status)) {
                Long complete = completeMap.getOrDefault(receiveId, 0L);
                completeMap.put(receiveId, complete + 1);
            }
        }
    }

    @Override
    public List<InspectRecordTrendVo> inspectRecordTrend(@NonNull Period period, @NonNull PeriodTypeEnum periodTypeEnum) {

        //获取时间段范围的巡检任务
        List<InspectRecordPO> recordList = recordCommandService.lambdaQuery()
                .between(InspectRecordPO::getTime, period.getBegin(), period.getEnd())
                .list();
        switch (periodTypeEnum) {

            case THIS_WEEK:
                return this.countInspectTaskCountByDate(recordList, (date) -> String.valueOf(date.getDayOfWeek()));
            case THIS_MONTH:
                return this.countInspectTaskCountByDate(recordList, this::dayOfMonth);
            case THIS_YEAR:
                return this.countInspectTaskCountByDate(recordList, this::monthOfYear);
            default:
                return CollectionUtils.emptyList();
        }
    }

    private List<InspectRecordTrendVo> countInspectTaskCountByDate(@NonNull List<InspectRecordPO> records,
                                                                   @NonNull Function<LocalDateTime, String> converter
                                                                  ) {
        //先设置总数
        List<InspectRecordTrendVo> inspectRecordTrendVoList = records.stream()
                .map(InspectRecordPO::getTime)
                .collect(Collectors.groupingBy(converter, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> InspectRecordTrendVo.builder().date(entry.getKey()).inspectNum(entry.getValue()).build())
                .collect(Collectors.toList());
        Map<String, Long> exceptionNumMap = records.stream()
                //保留异常的巡检任务
                .filter(recordPO -> {
                    InspectRecordResultEnum result = recordPO.getResult();
                    return InspectRecordResultEnum.ABNORMAL.equals(result);
                })
                .map(InspectRecordPO::getTime)
                .collect(Collectors.groupingBy(converter, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> InspectRecordTrendVo.builder().date(entry.getKey()).exceptionNum(entry.getValue()).build())
                .collect(Collectors.toMap(InspectRecordTrendVo::getDate, InspectRecordTrendVo::getExceptionNum));
        inspectRecordTrendVoList.forEach(vo -> vo.setExceptionNum(exceptionNumMap.getOrDefault(vo.getDate(), 0L)));
        return inspectRecordTrendVoList;
    }

    /**
     * 获取日期是否属于一个月的第几天
     *
     * @param dateTime 日期对象实例
     *
     * @return 1-31
     */

    private String dayOfMonth(LocalDateTime dateTime) {
        return String.valueOf(dateTime.getDayOfMonth());
    }

    /**
     * 获取日期是否属于一年中的第几月
     *
     * @param dateTime 日期对象实例
     *
     * @return 1-12
     */
    private String monthOfYear(LocalDateTime dateTime) {
        return String.valueOf(dateTime.getMonthValue());
    }
}