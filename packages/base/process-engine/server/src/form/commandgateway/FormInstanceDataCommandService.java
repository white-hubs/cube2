package com.guhecloud.cube.base.flow.form.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceDataCommand;
import com.guhecloud.cube.base.flow.form.po.FormInstanceDataPO;

/**
 * @author bq
 * @date 2022/09/23
 * @description
 */
public interface FormInstanceDataCommandService extends IService<FormInstanceDataPO> {

    /**
     * 保存或者修改表单实例数据（监听流程完成时或者填写纯业务表单时）
     *
     * @param formInstanceDataCommand 表单实例数据命令
     *
     * @return 业务实例数据json字符串
     */
    String saveOrUpdateData(FormInstanceDataCommand formInstanceDataCommand);

    /**
     * 根据表单控件与业务字段的映射关系，解析表单数据
     *
     * @param formInstanceId 表单实例ID
     * @param dataJson       表单实例数据json字符串
     *
     * @return 业务实例数据json字符串
     */
    String parseFormInstanceDataJson(Long formInstanceId, String dataJson);

}
