package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import lombok.Data;

/**
 * 巡检对象实体类
 */
@Data
public class InspectTargetDTO {
    /**
     * 巡检对象类型
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
     * 巡检对象编码
     */
    private String targetCode;

    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;
}
