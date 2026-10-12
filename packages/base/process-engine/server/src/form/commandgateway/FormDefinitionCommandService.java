package com.guhecloud.cube.base.flow.form.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.PublishFormDefinitionCommand;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;

/**
 * @author bq
 * @date 2022/09/16
 * @description
 */
public interface FormDefinitionCommandService extends IService<FormDefinitionPO> {

    /**
     * 发布表单定义
     *
     * @param publishFormDefinitionCommand 表单定义
     *
     * @return 表单定义ID
     */
    Long publish(PublishFormDefinitionCommand publishFormDefinitionCommand);

}
