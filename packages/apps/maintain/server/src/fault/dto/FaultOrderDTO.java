package com.guhecloud.cube.srv.gen.maintain.server.fault.dto;

import annotation.FormField;
import com.guhecloud.cube.property.management.client.property.vo.consumable.ConsumableUseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultOrderDTO {
    @FormField(label = "对象类型")
    private String objType;
    @FormField(label = "故障对象")
    private FaultObject faultObj;
    @FormField(label = "维修对象")
    private String repairObj;
    @FormField(label = "网格")
    private List<Long> areaId;
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
    @FormField(label = "所用耗材")
    private List<ConsumableUseInfo> consumable;

    @Data
    public static class FaultObject {
        private Long id;
        private String name;
        private String type;
        private Long areaId;
    }
}
