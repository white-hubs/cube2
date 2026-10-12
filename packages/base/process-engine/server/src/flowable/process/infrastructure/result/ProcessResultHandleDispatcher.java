package com.guhecloud.cube.base.flow.flowable.process.infrastructure.result;

import com.guhecloud.cube.base.flow.flowable.common.event.EventDispatcher;
import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author pengjian
 * @date 2022/10/15 10:57
 * @description
 */
@SuppressWarnings("EqualsHashCode")
public class ProcessResultHandleDispatcher {
    public final Logger log = LoggerFactory.getLogger(EventDispatcher.class);
    /**
     * 并发事件组，实现列表的并发更新
     */
    public CopyOnWriteArrayList<Entry> listenerHub = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<ProcessResultHandler> listenerHubAll = new CopyOnWriteArrayList<>();

    /**
     * 处理事件
     *
     * @param processInstanceContext
     */
    public void fireResult(ProcessInstanceContext processInstanceContext) {
        if (null == processInstanceContext.getResult()) {
            throw new IllegalArgumentException();
        }

        // 默认监听所有结果(如果listener的interest为空)
        listenerHubAll.stream().forEach(listener -> {
            listener.onEvent(processInstanceContext);
        });

        for (ProcessResultHandler listener : getEntry(processInstanceContext.getResult()).listeners) {
            try {
                listener.onEvent(processInstanceContext);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }

        }
    }

    /**
     * 注册监听事件
     *
     * @param listener
     */
    public void registerListener(ProcessResultHandler listener) {
        if (CollectionUtils.isEmpty(listener.interest())) {
            listenerHubAll.addIfAbsent(listener);
        } else {
            for (String result : listener.interest()) {
                getEntry(result).listeners.addIfAbsent(listener);
            }
        }

    }

    /**
     * 清空事件总线
     */
    public void clear() {
        listenerHub.clear();

    }

    /**
     * 获取事件组
     *
     * @param result
     *
     * @return
     */
    private Entry getEntry(String result) {
        for (; ; ) {
            for (Entry entry : listenerHub) {
                if (entry.result.equals(result)) {
                    return entry;
                }
            }
            Entry temp = new Entry(result);
            //如果不存在，才添加
            if (listenerHub.addIfAbsent(temp)) {
                return temp;
            }
        }

    }

    /**
     * 按照事件类型，设计一个监听组
     */
    private class Entry {
        final String result;
        CopyOnWriteArrayList<ProcessResultHandler> listeners;

        Entry(String result) {
            this.result = result;
            listeners = new CopyOnWriteArrayList<>();
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj || (obj.getClass() != this.getClass())) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return result.equals(((Entry) obj).result);
        }

    }

    /**
     * 单例控制 EventDispatcher
     */
    private static class ProcessResultHandleDispatcherFactory {
        public static ProcessResultHandleDispatcher _instance = new ProcessResultHandleDispatcher();
    }

    public static ProcessResultHandleDispatcher getInstance() {
        return ProcessResultHandleDispatcherFactory._instance;
    }

    private ProcessResultHandleDispatcher() {
    }
}
