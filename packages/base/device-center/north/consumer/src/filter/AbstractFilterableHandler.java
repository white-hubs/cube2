package com.guhecloud.cube.iot.north.bridge.consumer.filter;

import com.guhecloud.cube.iot.north.bridge.consumer.handler.MsgHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 通用过滤Handler抽象基类（装饰器/模板方法模式）。
 * <p>
 * 该类采用装饰器模式和模板方法模式，为各种类型的消息处理器提供统一的过滤能力。
 * 支持两级过滤：
 * <ul>
 *   <li>设备级过滤：根据设备ID进行过滤</li>
 *   <li>参数级过滤：根据属性code、事件code、命令id等参数进行过滤</li>
 * </ul>
 * <p>
 * <b>运行时动态管理</b>：支持在运行时动态添加/移除设备ID和参数code，无需重新创建Handler。
 * <p>
 * <b>线程安全性</b>：所有动态管理方法都是线程安全的，可以在多线程环境下安全调用。
 *
 * @param <T> 消息类型
 * @param <H> 委托的原始Handler类型
 */
public abstract class AbstractFilterableHandler<T, H extends MsgHandler> implements MsgHandler {

    /** 过滤条件封装对象 */
    protected final FilterCriteria criteria;

    /** 被装饰的原始消息处理器 */
    protected final H delegate;

    /** 设备ID过滤集合（直接引用，避免重复获取） */
    protected final Set<String> deviceFilter;

    /** 参数code过滤集合（属性code/事件code/命令id等，直接引用） */
    protected final Set<String> paramFilter;

    /** 读写锁（用于过滤逻辑的线程安全） */
    private final ReadWriteLock lock;

    /**
     * 构造函数。
     *
     * @param criteria 过滤条件
     * @param delegate 被装饰的原始消息处理器
     */
    protected AbstractFilterableHandler(FilterCriteria criteria, H delegate) {
        this.criteria = criteria;
        this.delegate = delegate;
        this.deviceFilter = criteria != null ? criteria.getDeviceIds() : Collections.emptySet();
        this.paramFilter = criteria != null ? criteria.getParamCodes() : Collections.emptySet();
        this.lock = criteria != null ? criteria.getLock() : null;
    }

    /**
     * 从消息中提取设备ID（用于设备级过滤）。
     * <p>
     * 模板方法，由子类实现具体的设备ID提取逻辑。
     *
     * @param msg 消息对象
     * @return 设备ID，如果无法提取则返回null
     */
    protected abstract String extractDeviceId(T msg);

    /**
     * 从消息中提取参数集合（属性code/事件code/命令id/状态key等，用于参数级过滤）。
     * <p>
     * 模板方法，由子类实现具体的参数提取逻辑。
     *
     * @param msg 消息对象
     * @return 参数code集合，如果无法提取则返回空集合
     */
    protected abstract Set<String> extractParams(T msg);

    /**
     * 判断是否应该处理该消息（线程安全）。
     * <p>
     * 执行两级过滤逻辑：
     * <ol>
     *   <li>快速路径：如果没有任何过滤条件，直接放行</li>
     *   <li>设备级过滤：如果配置了设备ID过滤，检查消息的设备ID是否在过滤列表中</li>
     *   <li>参数级过滤：如果配置了参数过滤，检查消息的参数集合是否与过滤列表有交集</li>
     * </ol>
     *
     * @param msg 消息对象
     * @return true表示应该处理该消息，false表示应该过滤掉
     */
    protected boolean shouldProcess(T msg) {
        if (lock == null) {
            // 无过滤条件，直接放行
            return true;
        }

        lock.readLock().lock();
        try {
            // 快速路径：无过滤条件直接放行
            if (deviceFilter.isEmpty() && paramFilter.isEmpty()) {
                return true;
            }

            // 设备级过滤（短路）
            if (!deviceFilter.isEmpty()) {
                String deviceId = extractDeviceId(msg);
                if (deviceId == null || !deviceFilter.contains(deviceId)) {
                    return false;
                }
            }

            // 参数级过滤（交集判断）
            if (!paramFilter.isEmpty()) {
                Set<String> msgParams = extractParams(msg);
                if (msgParams == null || msgParams.isEmpty()) {
                    return false;
                }
                for (String p : msgParams) {
                    if (paramFilter.contains(p)) {
                        return true;
                    }
                }
                return false;
            }

            return true;
        } finally {
            lock.readLock().unlock();
        }
    }

    // ========== 动态管理接口 ==========

    /**
     * 获取过滤条件对象，用于直接操作过滤条件。
     *
     * @return FilterCriteria实例
     */
    public FilterCriteria getFilterCriteria() {
        return criteria;
    }

    /**
     * 运行时添加单个设备ID到过滤条件。
     *
     * @param deviceId 设备ID
     */
    public void addDeviceId(String deviceId) {
        if (criteria != null) {
            criteria.addDeviceId(deviceId);
        }
    }

    /**
     * 运行时批量添加设备ID到过滤条件。
     *
     * @param deviceIds 设备ID集合
     */
    public void addDeviceIds(Collection<String> deviceIds) {
        if (criteria != null) {
            criteria.addDeviceIds(deviceIds);
        }
    }

    /**
     * 运行时从过滤条件中移除单个设备ID。
     *
     * @param deviceId 设备ID
     */
    public void removeDeviceId(String deviceId) {
        if (criteria != null) {
            criteria.removeDeviceId(deviceId);
        }
    }

    /**
     * 运行时从过滤条件中批量移除设备ID。
     *
     * @param deviceIds 设备ID集合
     */
    public void removeDeviceIds(Collection<String> deviceIds) {
        if (criteria != null) {
            criteria.removeDeviceIds(deviceIds);
        }
    }

    /**
     * 运行时清空所有设备ID过滤条件。
     * <p>
     * 清空后，将接收所有设备的消息（如果参数code过滤条件也为空）。
     */
    public void clearDeviceIds() {
        if (criteria != null) {
            criteria.clearDeviceIds();
        }
    }

    /**
     * 运行时添加单个参数code到过滤条件。
     *
     * @param paramCode 参数code（属性code、事件code等）
     */
    public void addParamCode(String paramCode) {
        if (criteria != null) {
            criteria.addParamCode(paramCode);
        }
    }

    /**
     * 运行时批量添加参数code到过滤条件。
     *
     * @param paramCodes 参数code集合
     */
    public void addParamCodes(Collection<String> paramCodes) {
        if (criteria != null) {
            criteria.addParamCodes(paramCodes);
        }
    }

    /**
     * 运行时从过滤条件中移除单个参数code。
     *
     * @param paramCode 参数code
     */
    public void removeParamCode(String paramCode) {
        if (criteria != null) {
            criteria.removeParamCode(paramCode);
        }
    }

    /**
     * 运行时从过滤条件中批量移除参数code。
     *
     * @param paramCodes 参数code集合
     */
    public void removeParamCodes(Collection<String> paramCodes) {
        if (criteria != null) {
            criteria.removeParamCodes(paramCodes);
        }
    }

    /**
     * 运行时清空所有参数code过滤条件。
     * <p>
     * 清空后，将接收所有参数的消息（如果设备ID过滤条件也为空）。
     */
    public void clearParamCodes() {
        if (criteria != null) {
            criteria.clearParamCodes();
        }
    }
}

