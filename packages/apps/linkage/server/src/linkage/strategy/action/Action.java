package com.guhecloud.cube.biz.linkage.server.linkage.strategy.action;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result.CallbackResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public abstract class Action<T extends CallbackResult> implements Supplier<CallbackResult> {

    protected CallbackResult callbackResult;

    protected LinkageActionTypeEnum actionType;

    @Getter protected Integer order;

    /**
     * 执行动作
     *
     * @return
     */
    protected abstract ExecuteStatusEnum executeAction();

    protected abstract ExecuteStatusEnum fail();

    @Override
    public CallbackResult get() {
        try {
            ExecuteStatusEnum executeStatus = this.executeAction();
            // 动作回调结果
            this.callbackResult.setExecuteStatus(executeStatus);
        } catch (Exception e) {
            log.error("{} 动作 执行异常，异常信息：{},不中断所有后续动作", actionType.getDescription(), e.getMessage());
            this.callbackResult = CallbackResult.FAIL;
        }
        return this.callbackResult;
    }

    /**
     * 失败
     *
     * @return
     */
    public CallbackResult getFail() {
        ExecuteStatusEnum executeStatus = this.fail();
        // 动作回调结果
        this.callbackResult.setExecuteStatus(executeStatus);
        return this.callbackResult;
    }
}
