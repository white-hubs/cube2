package com.guhecloud.cube.base.flow.form.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2022/10/25
 * @description 表单控件与流程节点配置操作权限
 */
@Data
@TableName("form_control_flow")
public class FormControlFlowPO {

    private Long id;

    /**
     * 流程定义key
     */
    private String flowKey;

    /**
     * 任务节点信息
     */
    private String taskKey;

    /**
     * json配置
     */
    private String config;

}