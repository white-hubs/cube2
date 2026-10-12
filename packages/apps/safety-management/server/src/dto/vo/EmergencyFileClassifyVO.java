package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 预案文件事件分类
 * @date 2023/10/8 20:02
 */
@Data
public class EmergencyFileClassifyVO {
    /**
     * 事件分类名称
     */
    private String classifyName;
    /**
     * 事件分类编号
     */
    private String classifyCode;
}
