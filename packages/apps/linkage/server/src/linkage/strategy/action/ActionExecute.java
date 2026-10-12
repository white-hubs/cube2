package com.guhecloud.cube.biz.linkage.server.linkage.strategy.action;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result.CallbackResult;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 联动动作组
 *
 * @author lanchun
 * @date 2022/10/28 13:53
 */
@Slf4j
public class ActionExecute {
    private List<Action<CallbackResult>> actions;

    public ActionExecute(List<Action<CallbackResult>> actions) {
        this.actions = actions;
    }

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            //maximumPoolSize: 池中允许的最大的线程数
            20,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingDeque<>(100000),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("linkageAction-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会使用拒绝策略。
            new ThreadPoolExecutor.AbortPolicy()
    );

    public void execute() {
        this.actions = actions.stream().sorted(Comparator.comparing(Action::getOrder)).collect(Collectors.toList());
        //其它动作
        for (Action<CallbackResult> action : actions) {
            executor.execute(() -> {
                CallbackResult callbackResult = action.get();
                EventActionService eventActionService = ApplicationContextHolder.getBean(EventActionService.class);
                eventActionService.updateStatus(callbackResult.getEventActionId(), callbackResult.getExecuteStatus());
            });
        }
    }
}
