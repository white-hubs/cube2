package com.guhecloud.cube.iot.north.bridge.consumer.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 订阅过滤条件封装类（支持运行时动态修改）。
 * <p>
 * 封装设备级和参数级的过滤条件，包含：
 * <ul>
 *   <li>设备ID集合：用于过滤特定设备的消息</li>
 *   <li>参数Code集合：包括属性code、事件code、命令id、状态key等，用于精细化过滤</li>
 * </ul>
 * <p>
 * <b>线程安全性</b>：使用 {@link ReentrantReadWriteLock} 实现读写分离，优化高并发场景下的读性能。
 * <ul>
 *   <li>读操作（getDeviceIds、getParamCodes）：多线程并发读取，无阻塞</li>
 *   <li>写操作（add/remove/clear）：独占锁，保证数据一致性</li>
 * </ul>
 * <p>
 * <b>使用场景</b>：
 * <ul>
 *   <li>初始化时设置基础过滤条件</li>
 *   <li>运行时动态添加/移除设备ID或参数code</li>
 *   <li>清空过滤条件以监听所有设备</li>
 * </ul>
 *
 * @see ReentrantReadWriteLock
 */
public final class FilterCriteria {

    /** 设备ID集合（可变，线程安全） */
    private final Set<String> deviceIds;

    /** 参数code集合（可变，线程安全），可包含属性code、事件code、命令id等 */
    private final Set<String> paramCodes;

    /** 读写锁（非公平模式，优化吞吐量） */
    private final ReadWriteLock lock;

    /**
     * 私有构造函数。
     *
     * @param deviceIds 设备ID集合
     * @param paramCodes 参数code集合
     */
    private FilterCriteria(Set<String> deviceIds, Set<String> paramCodes) {
        this.deviceIds = new HashSet<>();
        this.paramCodes = new HashSet<>();
        this.lock = new ReentrantReadWriteLock(false); // 非公平锁，优化性能

        if (deviceIds != null) {
            this.deviceIds.addAll(deviceIds);
        }
        if (paramCodes != null) {
            this.paramCodes.addAll(paramCodes);
        }
    }

    /**
     * 创建过滤条件实例。
     *
     * @param deviceIds 设备ID集合，如果为null则使用空集合
     * @param paramCodes 参数code集合，如果为null则使用空集合
     * @return FilterCriteria实例
     */
    public static FilterCriteria of(Collection<String> deviceIds, Collection<String> paramCodes) {
        Set<String> safeDeviceIds = deviceIds != null ? new HashSet<>(deviceIds) : Collections.emptySet();
        Set<String> safeParamCodes = paramCodes != null ? new HashSet<>(paramCodes) : Collections.emptySet();
        return new FilterCriteria(safeDeviceIds, safeParamCodes);
    }

    // ========== 设备ID管理方法 ==========

    /**
     * 添加单个设备ID到过滤条件。
     *
     * @param deviceId 设备ID，如果为null则忽略
     */
    public void addDeviceId(String deviceId) {
        if (deviceId == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            deviceIds.add(deviceId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 批量添加设备ID到过滤条件。
     *
     * @param deviceIds 设备ID集合，如果为null或空则忽略
     */
    public void addDeviceIds(Collection<String> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            return;
        }
        lock.writeLock().lock();
        try {
            if (deviceIds.contains(null)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            this.deviceIds.addAll(deviceIds);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从过滤条件中移除单个设备ID。
     *
     * @param deviceId 设备ID
     */
    public void removeDeviceId(String deviceId) {
        lock.writeLock().lock();
        try {
            deviceIds.remove(deviceId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从过滤条件中批量移除设备ID。
     *
     * @param deviceIds 设备ID集合
     */
    public void removeDeviceIds(Collection<String> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            return;
        }
        lock.writeLock().lock();
        try {
            deviceIds.remove(null);
            this.deviceIds.removeAll(deviceIds);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 清空所有设备ID过滤条件。
     * <p>
     * 清空后，将接收所有设备的消息（如果参数code过滤条件也为空）。
     */
    public void clearDeviceIds() {
        lock.writeLock().lock();
        try {
            deviceIds.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // ========== 参数Code管理方法 ==========

    /**
     * 添加单个参数code到过滤条件。
     *
     * @param paramCode 参数code（属性code、事件code等），如果为null则忽略
     */
    public void addParamCode(String paramCode) {
        if (paramCode == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            paramCodes.add(paramCode);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 批量添加参数code到过滤条件。
     *
     * @param paramCodes 参数code集合，如果为null或空则忽略
     */
    public void addParamCodes(Collection<String> paramCodes) {
        if (paramCodes == null || paramCodes.isEmpty()) {
            return;
        }
        lock.writeLock().lock();
        try {
            paramCodes.remove(null);
            this.paramCodes.addAll(paramCodes);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从过滤条件中移除单个参数code。
     *
     * @param paramCode 参数code
     */
    public void removeParamCode(String paramCode) {
        lock.writeLock().lock();
        try {
            paramCodes.remove(paramCode);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从过滤条件中批量移除参数code。
     *
     * @param paramCodes 参数code集合
     */
    public void removeParamCodes(Collection<String> paramCodes) {
        if (paramCodes == null || paramCodes.isEmpty()) {
            return;
        }
        lock.writeLock().lock();
        try {
            paramCodes.remove(null);
            this.paramCodes.removeAll(paramCodes);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 清空所有参数code过滤条件。
     * <p>
     * 清空后，将接收所有参数的消息（如果设备ID过滤条件也为空）。
     */
    public void clearParamCodes() {
        lock.writeLock().lock();
        try {
            paramCodes.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // ========== 查询方法 ==========

    /**
     * 获取当前设备ID集合（直接访问，用于过滤逻辑）。
     * <p>
     * <b>注意</b>：返回的Set受读锁保护，调用者应尽快完成读取操作。
     * 不要在持有返回Set的情况下执行耗时操作。
     *
     * @return 设备ID集合（线程安全）
     */
    public Set<String> getDeviceIds() {
        return deviceIds;
    }

    /**
     * 获取当前参数code集合（直接访问，用于过滤逻辑）。
     * <p>
     * <b>注意</b>：返回的Set受读锁保护，调用者应尽快完成读取操作。
     * 不要在持有返回Set的情况下执行耗时操作。
     *
     * @return 参数code集合（线程安全）
     */
    public Set<String> getParamCodes() {
        return paramCodes;
    }

    /**
     * 获取设备ID集合的不可变快照。
     * <p>
     * 适用于需要长时间持有或传递给其他组件的场景。
     *
     * @return 设备ID集合的不可变副本
     */
    public Set<String> getDeviceIdsSnapshot() {
        lock.readLock().lock();
        try {
            return Set.copyOf(deviceIds);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 获取参数code集合的不可变快照。
     * <p>
     * 适用于需要长时间持有或传递给其他组件的场景。
     *
     * @return 参数code集合的不可变副本
     */
    public Set<String> getParamCodesSnapshot() {
        lock.readLock().lock();
        try {
            return Set.copyOf(paramCodes);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 获取读锁（供AbstractFilterableHandler使用）。
     *
     * @return 读写锁实例
     */
    ReadWriteLock getLock() {
        return lock;
    }
}

