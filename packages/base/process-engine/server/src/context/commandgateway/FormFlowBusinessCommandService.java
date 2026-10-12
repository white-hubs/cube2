package com.guhecloud.cube.base.flow.context.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowBusinessCommand;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;

/**
 * @author bq
 * @date 2022/10/28
 * @description
 */
public interface FormFlowBusinessCommandService extends IService<FormFlowBusinessPO> {

    /**
     * 保存或修改
     *
     * @param formFlowBusinessCommand 关联信息
     */
    void saveOrUpdate(FormFlowBusinessCommand formFlowBusinessCommand);

}
