package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskUnitVO {
    /**
     * 风险单元 ID。
     */
    private Long id;

    /**
     * 单元编号。
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
     * 辨识状态，取值：IDENTIFYING=辨识中，PENDING_REVIEW=审核中，REVIEWED=已审核。
     */
    private String identifyStatus;

    /**
     * 当前辨识轮次号。
     */
    private String currentIdentifyRoundNo;

    /**
     * 最近辨识提交时间。
     */
    private LocalDateTime lastIdentifySubmitTime;

    /**
     * 最近辨识审核完成时间。
     */
    private LocalDateTime lastIdentifyReviewTime;

    /**
     * 下次重新辨识时间。
     */
    private LocalDateTime nextIdentifyTime;

    /**
     * 位置描述。
     */
    private String locationDesc;

    /**
     * 单元说明。
     */
    private String description;

    /**
     * 最新固有风险等级，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String inherentRiskLevel;

    /**
     * 最新控制风险等级，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String controlRiskLevel;

    /**
     * 单元当前风险等级，按单元下所有风险对象当前等级取最高值；对象当前等级优先取控制风险等级，缺失时取固有风险等级。
     */
    private String riskLevel;

    /**
     * 单元管控状态。
     */
    private String controlStatus;

    /**
     * 单元 GIS 点位 JSON。
     */
    private String gisPoint;

    /**
     * 风险对象数量。
     */
    private Long objectCount;

    /**
     * 退回对象数量。
     */
    private Long rejectedCount;

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
