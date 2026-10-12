package com.guhecloud.cube.biz.emergency.server.time.core.helper;

import com.guhecloud.cube.biz.emergency.server.time.core.thread.RuleTimeJobThread;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class EmergencyRuleTimeJobHelper {

    private static EmergencyRuleTimeJobHelper ruleTimeJobHelper = new EmergencyRuleTimeJobHelper();

    public static EmergencyRuleTimeJobHelper getRuleTimeJobHelper() {
        return ruleTimeJobHelper;
    }

    private RuleTimeJobThread ruleTimeJobThread;

    private Thread ruleTimeThread;

    public void start() {
        this.ruleTimeJobThread = new RuleTimeJobThread();
        ruleTimeThread = new Thread(ruleTimeJobThread);
        ruleTimeThread.setDaemon(true);
        ruleTimeThread.setName("queryNextRuleTimeJobThread");
        ruleTimeThread.start();
    }

    public void stop() {
        // 1、stop schedule
        ruleTimeJobThread.threadToStop();
        this.closeThread(ruleTimeThread);
    }

    private void closeThread(Thread thread) {
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        if (thread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
