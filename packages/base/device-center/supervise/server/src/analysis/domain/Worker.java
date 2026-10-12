package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author pengjian
 * @date 2022/7/25 18:34
 * @description
 */
@Slf4j
public abstract class Worker<T extends CallbackResult> implements Callable<T> {

    protected CallbackResult callbackResult;

    /**
     * 计算分数
     *
     * @return
     */
    protected abstract Integer computingScore();

    /**
     * 事件提醒
     */
    protected abstract void remind();

    @Override
    public T call() {
        try {
            Integer integer = computingScore();
            // 回调结果
            this.callbackResult.setScore(integer);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(this.getClass() + " worker 执行异常，异常信息：{},中断所有后续任务,执行失败回调", e.getMessage());

            //策略之一： 告诉group我失败了，group将会中断所有任务
            /**
             * todo 某些情况下，涉及到事务，我可能会想group保持一整个事务
             *
             * 这应该是根据策略来设置
             */
            this.callbackResult = CallbackResult.FAIL;

        }

        return (T) this.callbackResult;

    }

    public CallbackResult getCallbackResult() {
        return callbackResult;
    }
}
