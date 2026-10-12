package com.guhecloud.platform.rocketmq.context;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public final class RocketMQTransactionSynchronizationUtils {

    private RocketMQTransactionSynchronizationUtils() {
    }

    public static void executeAfterCommitOrNow(@NonNull Runnable action) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            action.run();
            return;
        }
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            log.warn("Transaction is active but synchronization is not active, execute RocketMQ action immediately");
            action.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    action.run();
                } catch (Exception e) {
                    log.error("Execute RocketMQ action after transaction commit failed", e);
                }
            }
        });
    }
}
