package com.guhecloud.cube.base.flow.form.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceCommand;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;

import java.util.Set;

/**
 * @author bq
 * @date 2022/09/08
 * @description
 */
public interface FormInstanceCommandService extends IService<FormInstancePO> {

    /**
     * 启动表单实例
     *
     * @param formInstanceCommand 表单实例
     *
     * @return 表单实例ID
     */
    Long start(FormInstanceCommand formInstanceCommand);

    /**
     * 删除表单定义
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除审批流程
     *
     * @param processInsIds
     */
    boolean batchDeleteProcessInstance(Set<String> processInsIds);
}
