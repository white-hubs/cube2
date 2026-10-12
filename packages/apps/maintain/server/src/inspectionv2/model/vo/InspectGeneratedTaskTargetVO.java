package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 已生成任务关联过的巡检对象。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectGeneratedTaskTargetVO {

    /**
     * 对象 id。
     */
    private Long id;

    /**
     * 对象名称。
     */
    private String name;

    /**
     * 对象编码。
     */
    private String code;

    /**
     * 对象类型。
     */
    private String type;

    /**
     * 对象类型名称。
     */
    private String typeName;

    /**
     * 经纬度。
     */
    private String latAndLon;

    /**
     * 经纬度地址。
     */
    private String location;
}
