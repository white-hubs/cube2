package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author pengjian
 * @date 2022/8/1 10:11
 * @description
 */
@Slf4j
@AllArgsConstructor
public class ScheduleMonitoringResult implements Runnable {

    private List<Future<CallbackResult>> futures;
    private WorkerGroup workerGroup;

    @Override
    public void run() {
        log.info("轮询group结果");
        MonitoringResult monitoringResult = new MonitoringResult(futures);
        List<CallbackResult> call = monitoringResult.call();
        long failCount = call.stream()
                .filter(callbackResult -> callbackResult == CallbackResult.FAIL).count();

        if (failCount > 0) {
            log.error("失败任务数量:{}", failCount);
            workerGroup.stop();
            return;
        }

        // 成功-进行结果合并
        this.workerGroup.setCallbackResults(call);
        workerGroup.stop();
    }
}
