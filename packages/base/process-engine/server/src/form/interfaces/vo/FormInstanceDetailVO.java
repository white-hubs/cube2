package com.guhecloud.cube.base.flow.form.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例详情
 */
@Data
public class FormInstanceDetailVO {

    /**
     * 实例ID
     */
    private Long id;

    /**
     * 表单ID
     */
    private Long formFlowId;

    /**
     * 表单定义key
     */
    private String formKey;

    /**
     * 表单版本号
     */
    private int version;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务key
     */
    private String businessKey;

    //TODO 目前考虑的是一个表单只有一个数据，有可能会是同一个表单多人填多个数据，留下一个埋点，到时候调整
    /**
     * 表单实例数据
     */
    private FormInstanceDataVO formInstanceDataVO;

    /**
     * 表单定义信息
     */
    private FormDefinitionVO formDefinitionVO;

}
