package com.guhecloud.cube.base.flow.context.submit.commandgateway.command;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bq
 * @date 2023/01/11
 * @description 再次发起
 */
@Data
public class SubmitAgainFormCommand {

    /**
     * 表单实例ID
     */
    private Long formInstanceId;

    /**
     * 自定义统一显示待办名称
     */
    private String backLogTitle;

    /**
     * 表单数据
     */
    private String data;

    private Map<String, Object> variables = new HashMap<>();

}
