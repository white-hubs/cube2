package com.guhecloud.cube.srv.gen.maintain.server.fault.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 李杰
 * @describe 投诉建议
 * @date 2024/10/09 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ComplaintOrderDTO {
    @FormField(label = "投诉内容")
    private String repairObj;
    @FormField(label = "投诉类型")
    private Long faultType;
    @FormField(label = "严重程度")
    private String urgencyLevel;
    @FormField(label = "解决结果")
    private String result;
    @FormField(label = "投诉确认")
    private String confirm;
}
