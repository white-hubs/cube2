package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RiskUnitParam {
    /**
     * 单元编号；新增时未传则后端按 RU-{id} 生成，修改时未传保持原编号。
     */
    private String code;

    /**
     * 单元名称。
     */
    private String name;

    /**
     * 所属区域 ID。
     */
    private Long areaId;

    /**
     * 单元类型 ID。
     */
    private Long unitTypeId;

    /**
     * 责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 辨识周期，单位为天。
     */
    private Integer identifyCycleDays;

    /**
     * 位置描述。
     */
    private String locationDesc;

    /**
     * 单元说明，可选，最多 500 字。
     */
    private String description;

    /**
     * 单元 GIS 点位 JSON；可直接传完整地图点位结构。
     * 若未传，可传 longitude、latitude，由后端组装为 {"longitude":x,"latitude":y} 保存。
     */
    private String gisPoint;

    /**
     * 经度，GIS 地图选点结果；与 latitude 同时传入时可替代 gisPoint。
     */
    private BigDecimal longitude;

    /**
     * 纬度，GIS 地图选点结果；与 longitude 同时传入时可替代 gisPoint。
     */
    private BigDecimal latitude;

    /**
     * 安全警示标志 ID，新增/编辑风险单元时只能选择一个。
     */
    private Long warningSignId;

    /**
     * 安全警示标志 ID，兼容旧字段名；推荐使用 warningSignId。
     */
    private Long signId;

    /**
     * 单元辨识人员用户 ID 集合。
     */
    private List<String> identifierUserIds;
}
