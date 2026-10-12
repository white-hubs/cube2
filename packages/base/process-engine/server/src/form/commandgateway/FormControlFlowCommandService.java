package com.guhecloud.cube.base.flow.form.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlFlowCommand;
import com.guhecloud.cube.base.flow.form.po.FormControlFlowPO;

/**
 * @author bq
 * @date 2022/10/25
 * @description
 */
public interface FormControlFlowCommandService extends IService<FormControlFlowPO> {

    /**
     * 保存接口
     * 保存的时候清除之前的，保留最新的
     *
     * @param formControlFlowCommand
     */
    Long save(FormControlFlowCommand formControlFlowCommand);

}
