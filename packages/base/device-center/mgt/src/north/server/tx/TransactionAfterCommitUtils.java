package com.guhecloud.cube.iot.mgt.north.server.tx;

import com.guhecloud.cube.iot.mgt.core.data.exception.IotBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

/**
 * 事务提交后处理工具类
 * 用于在Spring事务提交后执行指定的回调操作
 */
@Slf4j
public class TransactionAfterCommitUtils {

    /**
     * 注册事务提交后执行的回调
     * 
     * @param callback 回调函数
     */
    public static void executeAfterCommit(TransactionAfterCommitCallback callback) {
        executeAfterCommit(callback, null);
    }
    
    /**
     * 注册事务提交后执行的回调（带异常处理器）
     * 
     * @param callback 回调函数
     * @param exceptionHandler 异常处理器
     */
    public static void executeAfterCommit(TransactionAfterCommitCallback callback, 
                                        Consumer<Exception> exceptionHandler) {
        if (callback == null) {
            throw new IotBusinessException("回调函数为空，忽略注册");
        }
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IotBusinessException("当前没有活跃的事务");
        }
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    callback.execute();
                } catch (Exception e) {
                    if (exceptionHandler != null) {
                        exceptionHandler.accept(e);
                    } else {
                        log.error("执行事务提交后回调时发生异常", e);
                    }
                }
            }
            
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_ROLLED_BACK) {
                    log.info("事务已回滚，跳过回调执行");
                }
            }
        });
        
        log.debug("成功注册事务提交后回调（带异常处理器）");
    }
    
    /**
     * 检查当前是否有活跃的事务
     * 
     * @return 是否有活跃事务
     */
    public static boolean hasActiveTransaction() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}