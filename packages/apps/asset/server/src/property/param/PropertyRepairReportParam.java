package com.guhecloud.cube.srv.gen.property.management.server.property.param;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PropertyRepairReportParam {
    @FormField(label = "对象类型")
    private String objType;
    @FormField(label = "故障对象")
    private Long[] faultObj;
    @FormField(label = "故障描述")
    private String faultDescribe;
    @FormField(label = "故障确认")
    private String confirm;
    @FormField(label = "故障类型")
    private Long faultType;
    @FormField(label = "紧急程度")
    private String urgencyLevel;
    @FormField(label = "确认描述")
    private String confirmDescribe;
    @FormField(label = "解决结果")
    private String result;
    @FormField(label = "维护成本")
    private Double cost;
    @FormField(label = "解决描述")
    private String solveDescribe;
}
