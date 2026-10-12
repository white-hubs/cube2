package com.guhecloud.platform.rocketmq.context;

import org.junit.After;
import org.junit.Test;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class RocketMQTransactionSynchronizationUtilsTest {

    @After
    public void clearTransactionSynchronization() {
        TransactionSynchronizationManager.clear();
    }

    @Test
    public void executeAfterCommitOrNowRunsImmediatelyWhenNoTransactionExists() {
        AtomicInteger executionCount = new AtomicInteger();

        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(executionCount::incrementAndGet);

        assertEquals(1, executionCount.get());
    }

    @Test
    public void executeAfterCommitOrNowDefersActionUntilActiveTransactionCommits() {
        AtomicInteger executionCount = new AtomicInteger();
        beginTransaction();

        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(executionCount::incrementAndGet);

        assertEquals(0, executionCount.get());
        TransactionSynchronizationManager.getSynchronizations()
                .forEach(TransactionSynchronization::afterCommit);
        assertEquals(1, executionCount.get());
    }

    @Test
    public void executeAfterCommitOrNowSkipsActionWhenActiveTransactionRollsBack() {
        AtomicInteger executionCount = new AtomicInteger();
        beginTransaction();

        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(executionCount::incrementAndGet);

        TransactionSynchronizationManager.getSynchronizations()
                .forEach(synchronization -> synchronization.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK));
        assertEquals(0, executionCount.get());
    }

    @Test
    public void executeAfterCommitOrNowKeepsLaterCallbacksWhenAfterCommitActionFails() {
        AtomicInteger executionCount = new AtomicInteger();
        beginTransaction();

        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(() -> {
            throw new IllegalStateException("send failed");
        });
        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(executionCount::incrementAndGet);

        TransactionSynchronizationManager.getSynchronizations()
                .forEach(TransactionSynchronization::afterCommit);
        assertEquals(1, executionCount.get());
    }

    private static void beginTransaction() {
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);
    }
}
