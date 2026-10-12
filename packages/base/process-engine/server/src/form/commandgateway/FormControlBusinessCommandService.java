package com.guhecloud.cube.base.flow.form.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlBusinessCommand;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;

/**
 * @author bq
 * @date 2022/10/25
 * @description
 */
public interface FormControlBusinessCommandService extends IService<FormControlBusinessPO> {

    /**
     * 保存或者修改
     *
     * @param formControlBusinessCommand
     */
    Long saveOrUpdate(FormControlBusinessCommand formControlBusinessCommand);

}
