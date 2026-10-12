package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/1 9:52
 * @description
 */
public class MonitoringResult {
    private List<Future<CallbackResult>> futures;
    private List<CallbackResult> results;

    public MonitoringResult(List<Future<CallbackResult>> futures) {
        this.futures = futures;
        this.results = new ArrayList<>(futures.size());
    }

    public List<CallbackResult> call() {
        for (int i = 0; i < futures.size(); i++) {
            Future<CallbackResult> future = futures.get(i);
            try {
                boolean done = future.isDone();
                if (done) {
                    CallbackResult callbackResult = future.get();
                    results.add(callbackResult);
                    if (callbackResult == CallbackResult.FAIL) {
                        // 有失败的任务，需要将未完成的任务全部取消
                        List<Future<CallbackResult>> unfinished = findUnfinished(futures);
                        cancel(unfinished);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private List<Future<CallbackResult>> findUnfinished(List<Future<CallbackResult>> futures) {
        return futures.stream().filter(future -> !future.isDone()).collect(Collectors.toList());
    }

    public void cancel(List<Future<CallbackResult>> futures) {
        //        futures.forEach(future -> future.cancel(true));
    }
}
