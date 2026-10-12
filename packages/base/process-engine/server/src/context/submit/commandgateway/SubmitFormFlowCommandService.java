package com.guhecloud.cube.base.flow.context.submit.commandgateway;

import com.guhecloud.cube.base.flow.context.submit.commandgateway.command.SubmitAgainFormCommand;
import instance.command.SubmitInitFormCommand;

/**
 * @author bq
 * @date 2022/11/10
 * @description
 */
public interface SubmitFormFlowCommandService {

    /**
     * 前端第一次提交表单数据
     *
     * @param submitInitFormCommand 初始提交数据
     *
     * @return
     */
    String submitFirstForm(SubmitInitFormCommand submitInitFormCommand);

    /**
     * 后端调用，再次发起
     */
    void submitAgain(SubmitAgainFormCommand againFormCommand);

    /**
     * 服务间调用进行发起流程
     *
     * @param formFlowCommand 提交数据
     *
     * @return 流程id
     */
    String submitInnerServerForm(SubmitInitFormCommand formFlowCommand);
}
