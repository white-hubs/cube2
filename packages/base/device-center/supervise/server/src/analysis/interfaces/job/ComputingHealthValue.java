package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.job;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceHealthValueService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.HealthAlarmConfigService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.HealthAlarmTrigger;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.Callback;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.CallbackResult;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.WorkerGroup;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.AlarmWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.DeviceStatusWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.FaultWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.HiddenDangerWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.InspectionWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.LifeCycleWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.OutsourceRepairWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker.UpKeepWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthValuePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthAlarmRuleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/1 15:58
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComputingHealthValue {

    private final DeviceHealthValueService deviceHealthValueService;
    private final static ReentrantLock lock = new ReentrantLock();
    private final static Condition condition = lock.newCondition();
    private final TaskExecutor taskExecutor;
    //    private final DeviceAccountMapper deviceAccountMapper;
    private final HealthAlarmTrigger healthAlarmTrigger;
    private final HealthAlarmConfigService healthAlarmConfigService;
    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 异步更新
     */
    private static final ArrayBlockingQueue queue = new ArrayBlockingQueue<>(1000);

    //    @PostConstruct
    public void updateHealthValue() {
        taskExecutor.execute(() -> {
            lock.lock();
            try {
                while (true) {
                    // 挂起线程
                    List<DeviceHealthValuePO> batchUpdateList = new ArrayList<>(500);
                    queue.drainTo(batchUpdateList, 500);
                    if (CollectionUtils.isNotEmpty(batchUpdateList)) {
                        deviceHealthValueService.saveBatch(batchUpdateList);
                    }
                    condition.await();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        });
    }

    /**
     * 报警
     * 1. 读取报警配置
     * 2. 报警触发
     */
    public void alarm(Long deviceId, Integer healthValue) {
        if (isNeedAlarm(deviceId, healthValue)) {
            // 触发报警
            healthAlarmTrigger.triggered(deviceId, new HealthAlarmTrigger.EventConfig(), healthValue);
        }
    }

    private boolean isNeedAlarm(Long deviceId, Integer healthValue) {
        HealthAlarmRuleVO alarmRuleVO = healthAlarmConfigService.getByDeviceInstanceId(deviceId);
        if (Objects.nonNull(alarmRuleVO) && Boolean.TRUE.equals(alarmRuleVO.getEnable()) &&
            healthValue <= alarmRuleVO.getThresholdValue()) {
            return true;
        }
        return false;
    }

    @JobAnnotation(name = "computingHealthValue", cron = "0 0 0/1 * * ?", desc = "设备健康检查")
    public void schedule(String param) {
        var supplier = new Supplier() {
            private volatile Integer page = 1;

            @Override
            public List<DeviceBaseArchivesInfoDTO> get() {
                var qry = new DeviceBaseInfoPageQry();
                qry.setPageNumber(page);
                qry.setPageSize(1000);
                var page = deviceCenterStdClient.pageOfBaseInfo(qry);
                this.page++;
                return page.getRecords();
            }
        };

        while (true) {
            var data = supplier.get();
            if (CollectionUtils.isEmpty(data)) {
                break;
            }
            executeComputing(data, true);
        }
    }

    public void executeComputing(List<DeviceBaseArchivesInfoDTO> data, boolean isDelay) {
        data = data.stream().filter(v -> Objects.nonNull(v)).collect(Collectors.toList());
        List<DeviceHealthValuePO> batchCheckList = new ArrayList<>();
        data.forEach(deviceAccountPageVO -> {
            SessionContextHolder.requestTenant(deviceAccountPageVO.getTenant());
            DeviceHealthValuePO deviceHealthValuePO = new DeviceHealthValuePO();
            deviceHealthValuePO.setDeviceId(deviceHealthValuePO.getDeviceId());
            batchCheckList.add(deviceHealthValuePO);

            try {
                WorkerGroup workerGroup = buildWorkerGroup(
                        deviceAccountPageVO.getId(),
                        deviceHealthValuePO.getId(),
                        deviceAccountPageVO.getTenant()
                                                          );
                workerGroup.execute(new Callback() {
                    @Override
                    public void success(CallbackResult callbackResult) {
                        Integer score = callbackResult.getScore();
                        DeviceHealthValuePO deviceHealthValue = new DeviceHealthValuePO();
                        int finallyScore = 100 - score;
                        if (finallyScore < 0) {
                            finallyScore = 0;
                        }
                        deviceHealthValue.setHealthValue(finallyScore);
                        deviceHealthValue.setId(callbackResult.getHealthValueId());
                        deviceHealthValue.setDeviceId(callbackResult.getDeviceId());
                        deviceHealthValue.setTenant(callbackResult.getTenantId());
                        // 检测是否需要报警
                        SessionContextHolder.requestTenant(callbackResult.getTenantId());
                        alarm(deviceHealthValue.getDeviceId(), finallyScore);

                        // 进入本地队列批量消费
                        deviceHealthValueService.save(deviceHealthValue);
                    }

                    @Override
                    public void fail(CallbackResult callbackResult) {

                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void signal(boolean isDelay) {
        lock.lock();
        Integer takeEffect = 1;
        if (isDelay) {
            if (queue.size() >= takeEffect) {
                condition.signal();
            }
        } else {
            if (queue.size() >= 20) {
                condition.signal();
            }
        }

        lock.unlock();
    }

    private WorkerGroup buildWorkerGroup(Long deviceId, Long healthValueId, Integer tenantId) throws InterruptedException {
        Requester requester = SessionContextHolder.getRequester(true);
        Principal principal = SecurityContextHolder.getPrincipal(true);
        LifeCycleWorker lifeCycleWorker = new LifeCycleWorker(deviceId, healthValueId, tenantId, requester, principal);
        DeviceStatusWorker deviceStatusWorker = new DeviceStatusWorker(deviceId, healthValueId, tenantId, requester, principal);
        AlarmWorker alarmWorker = new AlarmWorker(deviceId, healthValueId, tenantId, requester, principal);
        FaultWorker faultWorker = new FaultWorker(deviceId, healthValueId, tenantId, requester, principal);
        InspectionWorker inspectionWorker = new InspectionWorker(deviceId, healthValueId, tenantId, requester, principal);
        UpKeepWorker upKeepWorker = new UpKeepWorker(deviceId, healthValueId, tenantId, requester, principal);
        HiddenDangerWorker hiddenDangerWorker = new HiddenDangerWorker(deviceId, healthValueId, tenantId, requester, principal);
        OutsourceRepairWorker outsourceRepairWorker = new OutsourceRepairWorker(deviceId, healthValueId, tenantId, requester, principal);

        WorkerGroup workerGroup = new WorkerGroup(
                tenantId,
                lifeCycleWorker,
                deviceStatusWorker,
                alarmWorker,
                faultWorker,
                inspectionWorker,
                upKeepWorker,
                hiddenDangerWorker,
                outsourceRepairWorker
        );

        return workerGroup;
    }

}
