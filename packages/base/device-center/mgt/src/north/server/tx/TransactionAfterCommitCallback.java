package com.guhecloud.cube.iot.mgt.north.server.tx;

/**
 * 事务提交后回调接口
 */
@FunctionalInterface
public interface TransactionAfterCommitCallback {

    /**
     * 事务提交后执行的方法
     */
    void execute();
}