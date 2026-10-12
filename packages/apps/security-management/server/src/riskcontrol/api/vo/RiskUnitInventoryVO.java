package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskUnitInventoryVO {
    /**
     * 风险单元记录 ID。
     */
    private Long id;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险单元编号。
     */
    private String code;

    /**
     * 风险单元名称。
     */
    private String name;

    /**
     * 所属区域 ID。
     */
    private Long areaId;

    /**
     * 所属区域引用对象，由 areaId 解析得到；code=区域编号，name=区域名称。
     */
    private RiskOptionVO area;

    /**
     * 单元类型 ID。
     */
    private Long unitTypeId;

    /**
     * 单元类型引用对象，由 unitTypeId 解析得到；code=类型编号，name=类型名称。
     */
    private RiskOptionVO unitType;

    /**
     * 责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 责任人引用对象，由 responsibleUserId 解析得到。
     */
    private RiskUserRefVO responsibleUser;

    /**
     * 辨识周期，单位为天。
     */
    private Integer identifyCycleDays;

    /**
     * 单元辨识状态。
     */
    private String identifyStatus;

    /**
     * 最新固有风险等级。
     */
    private String inherentRiskLevel;

    /**
     * 最新控制风险等级。
     */
    private String controlRiskLevel;

    /**
     * 单元管控状态。
     */
    private String controlStatus;

    /**
     * 下次重新辨识时间。
     */
    private LocalDateTime nextIdentifyTime;

    /**
     * 单元 GIS 点位 JSON。
     */
    private String gisPoint;

    /**
     * 位置描述。
     */
    private String locationDesc;

    /**
     * 单元说明。
     */
    private String description;

    /**
     * 风险对象数量。
     */
    private Long objectCount;

    /**
     * 管控措施数量。
     */
    private Long measureCount;

    /**
     * 已完成管控措施数量。
     */
    private Long completedMeasureCount;

    /**
     * 活跃隐患数量。
     */
    private Long hiddenDangerCount;

    /**
     * 安全警示标志 ID；风险单元只能关联一个安全警示标志。
     */
    private Long warningSignId;

    /**
     * 安全警示标志引用对象，由 warningSignId 解析得到，用于前端展示图标和名称。
     */
    private RiskWarningSignVO warningSign;

    /**
     * 单元辨识人员用户 ID 集合。
     */
    private List<String> identifierUserIds;

    /**
     * 单元辨识人员引用对象集合，由 identifierUserIds 解析得到。
     */
    private List<RiskUserRefVO> identifierUsers;
}
