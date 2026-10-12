package com.guhecloud.cube.base.alarm.server.flow.application.process.step;

import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.model.result.ProcessResult;

public interface AlarmProcessStep {

    /**
     * 执行步骤
     *
     * @param context 流程上下文
     *
     * @return 是否继续执行下一步
     */
    ProcessResult execute(ProcessContext context);

    /**
     * 获取步骤名称（用于日志和调试）
     */
    String getName();
}
