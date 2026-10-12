package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.enumeration.DeviceTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlInfoService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TimeControlJob {
    @Autowired
    private TimeControlInfoService timeControlInfoService;

    @Autowired
    private TimeControlDetailsService timeControlDetailsService;

    @Autowired
    private GroupBaseDetailsService groupBaseDetailsService;

    @Autowired
    private DeviceCenterAttrCtlStdClient deviceCenterAttrCtlClient;

    @Autowired
    private TimeControlLogService timeControlLogService;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                                                                                 //@param maximumPoolSize: 池中允许的最大的线程数
                                                                                 10,
                                                                                 //@param keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
                                                                                 5,
                                                                                 //@param 时间单位
                                                                                 TimeUnit.SECONDS,
                                                                                 //@param workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
                                                                                 new LinkedBlockingDeque<>(100000),
                                                                                 //@param threadFactory: 创建线程的工厂，比如指定线程名等
                                                                                 Executors.defaultThreadFactory(),
                                                                                 //@param handler: 拒绝策略，如果线程满了，线程池就会使用拒绝策略。
                                                                                 new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 时控计划失效Job
     */
    @JobAnnotation(name = "timeControlInvalidJob", cron = "0 0 0 * * ?", desc = "时控计划失效任务")
    public void executeTimeControlInvalidJob(String param) {
        var date = LocalDateTime.now();
        try {
            var time = DateUtils.format(date, "yyyy-MM-dd 00:00:00");
            //启用的计划
            var list = timeControlInfoService.list(Wrappers.<TimeControlInfo>lambdaQuery()
                                                           .eq(TimeControlInfo::getEnabled, true)
                                                           .le(TimeControlInfo::getPlanEndTime, time));
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(s -> s.setEnabled(false));
            }
            timeControlInfoService.updateBatchById(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 时控任务Job
     */
    @JobAnnotation(name = "timeControlJob", cron = "0 * * * * ?", desc = "时控任务Job")
    public void executeTimeControlJob(String param) {
        try {
            var date = LocalDateTime.now();
            var format = DateUtils.format(date, "HH:mm");
            var control = DateUtils.format(date, "yyyy-MM-dd HH:mm:00");
            var controlTime = DateUtils.parse(control);
            var beginOfDay = DateUtils.beginOfDay(controlTime);
            var i = DateUtils.dayOfWeekLegacy(date);
            //启用的计划
            var list = timeControlInfoService.list(Wrappers.<TimeControlInfo>lambdaQuery()
                                                           .eq(TimeControlInfo::getEnabled, true)
                                                           .like(TimeControlInfo::getTimeCycle, i)
                                                           .and(q -> q
                                                                   .le(TimeControlInfo::getPlanStartTime, beginOfDay)
                                                                   .or()
                                                                   .isNull(TimeControlInfo::getPlanStartTime))
                                                  );
            var ids = list.stream().map(TimeControlInfo::getId).collect(Collectors.toList());
            //计划的详情
            var timeControlDetails = timeControlDetailsService.list(Wrappers.<TimeControlDetails>lambdaQuery()
                                                                            .in(TimeControlDetails::getTimeControlId, ids)
                                                                            .eq(TimeControlDetails::getControlTime, format));

            threadPoolExecutor.execute(() -> {
                timeControlDetails.forEach(timeControlDetail -> {
                                               try {
                                                   timeControl(timeControlDetail, controlTime);
                                               } catch (Exception e) {
                                                   log.error("执行时控任务失败: {}", e.getMessage());
                                               }
                                           }
                                          );
            });
        } catch (Exception e) {
            log.error("时控任务失败", e);
        }
    }

    /**
     * @Description 控制
     * @Author lrj
     */
    private void timeControl(TimeControlDetails timeControlDetail, LocalDateTime controlTime) {
        String result = "";
        //控制参数
        String expectedValue = "";
        String originalValue = "";
        if (timeControlDetail.getOpenGate().equals("1")) {
            expectedValue = timeControlDetail.getOpenGate();
            originalValue = "0";
        } else if (timeControlDetail.getOpenGate().equals("0")) {
            expectedValue = timeControlDetail.getOpenGate();
            originalValue = "1";
        }
        Set<DevicePropWriteCmd.PropWriteValue> keyValues = new HashSet<>();
        DevicePropWriteCmd controlAttributeCommand = new DevicePropWriteCmd();
        DevicePropWriteCmd.PropWriteValue keyValue = new DevicePropWriteCmd.PropWriteValue();
        keyValue.setCode("disjunctor");
        keyValue.setExpectedValue(expectedValue);
        keyValue.setOriginalValue(originalValue);
        keyValues.add(keyValue);
        controlAttributeCommand.setPropWriteValues(keyValues);
        if (DeviceTypeEnum.DEVICE_GROUP.name().equals(timeControlDetail.getObjectType())) {
            List<GroupBaseDetails> list = groupBaseDetailsService.list(Wrappers.<GroupBaseDetails>lambdaQuery()
                                                                               .eq(GroupBaseDetails::getGroupControlId, timeControlDetail.getDeviceObject()));
            if (CollectionUtils.isNotEmpty(list)) {
                Integer total = list.size();
                Integer sumCount = 0;
                for (GroupBaseDetails groupBaseDetails : list) {
                    controlAttributeCommand.setDeviceId(groupBaseDetails.getDeviceId());
                    try {
                        deviceCenterAttrCtlClient.controlAttribute(controlAttributeCommand);
                        sumCount += 1;
                    } catch (Exception e) {
                        log.info("群控失败");
                    }
                }
                if (total.equals(sumCount)) {
                    result = "成功";
                } else {
                    result = "失败";
                }

            }

        } else {
            controlAttributeCommand.setDeviceId(timeControlDetail.getDeviceObject());
            try {
                deviceCenterAttrCtlClient.controlAttribute(controlAttributeCommand);
                result = "成功";
            } catch (Exception e) {
                log.info("设备控制失败");
                result = "成功";
            }
        }
        SessionContextHolder.requestTenant(timeControlDetail.getTenant());
        TimeControlLog timeControlLog = new TimeControlLog();
        timeControlLog.setControlTime(controlTime);
        timeControlLog.setTimeControlId(timeControlDetail.getTimeControlId());
        timeControlLog.setDeviceObject(timeControlDetail.getDeviceObject());
        timeControlLog.setBrightness(timeControlDetail.getBrightness());
        timeControlLog.setObjectName(timeControlDetail.getDeviceName());
        timeControlLog.setObjectType(timeControlDetail.getObjectType());
        timeControlLog.setHistoryTime(timeControlDetail.getControlTime());
        timeControlLog.setOpenGate(timeControlDetail.getOpenGate().equals("1") ? "开" : "关");
        timeControlLog.setResult(result);
        timeControlLogService.save(timeControlLog);
    }

}
