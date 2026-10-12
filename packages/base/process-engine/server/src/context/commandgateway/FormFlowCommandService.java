package com.guhecloud.cube.base.flow.context.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;

/**
 * @author bq
 * @date 2022/09/08
 * @description
 */
public interface FormFlowCommandService extends IService<FormFlowPO> {

    /**
     * 保存或修改表单流程信息
     *
     * @param formFlowCommand 表单流程信息
     *
     * @return 表单流程信息ID
     */
    Long saveOrUpdate(FormFlowCommand formFlowCommand);

    /**
     * 更新表单流程的使用状态
     *
     * @param formFlowId   表单流程Id
     * @param actionRemove 是否是删除操作
     */
    void updateStatus(Long formFlowId, boolean actionRemove);

    /**
     * 表单流程开启或停用
     *
     * @param formFlowId 表单流程ID
     * @param enable     开/关
     */
    Boolean enable(Long formFlowId, Boolean enable);

    /**
     * 删除表单流程
     *
     * @param formFlowId 表单流程ID
     */
    Boolean delete(Long formFlowId);

}
