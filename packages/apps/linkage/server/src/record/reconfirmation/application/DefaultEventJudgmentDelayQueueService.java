package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.entity.EventJudgmentDelayTask;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DefaultEventJudgmentDelayQueueService implements EventJudgmentDelayQueueService {

    private final ScheduledExecutorService scheduler;
    private final ExecutorService workerPool;
    private final Map<Long, ScheduledFuture<?>> pendingTasks = new ConcurrentHashMap<>();
    private static final Integer DEFAULT_TIMEOUT_SECONDS = 30;

    public DefaultEventJudgmentDelayQueueService() {
        var workerPoolSize = 1000;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder()
                        .setNameFormat("event-judgment-delay-queue-%d")
                        .build());
        this.workerPool = new ThreadPoolExecutor(
                workerPoolSize,
                workerPoolSize * 2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(workerPoolSize),
                new ThreadFactoryBuilder().setNameFormat("event-judgment-timeout-task-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Override
    public void submitDelayTask(EventJudgmentDelayTask task) {
        var timeoutSeconds = task.getDelaySeconds();
        if (Objects.isNull(timeoutSeconds) || timeoutSeconds <= 0) {
            timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
        }
        var future = scheduler.schedule(() -> {
            try {
                workerPool.execute(task.getTimeoutCallback());
            } finally {
                pendingTasks.remove(task.getId());
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
        pendingTasks.put(task.getId(), future);
        log.info("Submitted delay task for alarm {},judgment delay {} seconds", task.getId(), task.getDelaySeconds());
    }

    @Override
    public void cancelDelayTask(Long id) {
        var future = pendingTasks.get(id);
        if (future != null) {
            boolean cancelled = future.cancel(false);
            if (cancelled) {
                pendingTasks.remove(id, future); // 原子性移除
                log.info("Successfully cancelled task for alarm {}", id);
            } else {
                log.warn("Task for alarm {} may already be running", id);
            }
        } else {
            log.debug("No pending task found for alarm {}", id);
        }
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
        workerPool.shutdown();
    }
}
