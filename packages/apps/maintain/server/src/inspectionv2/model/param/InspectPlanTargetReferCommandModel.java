package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class InspectPlanTargetReferCommandModel {

    /**
     * 巡检对象类型：device设备 stall挡位 等
     */
    private InspectTargetCategoryEnum targetCategory;

    /**
     * 巡检对象具体类型
     */
    private String targetType;

    /**
     * 巡检对象ID
     */
    private Long targetId;

    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 巡检标准
     */
    private List<String> standardIds;

}
