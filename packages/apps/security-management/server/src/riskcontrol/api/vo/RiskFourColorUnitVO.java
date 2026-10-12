package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskFourColorUnitVO {
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
     * 区域所属部门 ID。
     */
    private String departmentId;

    /**
     * 区域所属部门引用对象，由 departmentId 解析得到。
     */
    private RiskDepartmentRefVO department;

    /**
     * 责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 责任人引用对象，由 responsibleUserId 解析得到。
     */
    private RiskUserRefVO responsibleUser;

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
     * 风险对象数量。
     */
    private Long objectCount;

    /**
     * 管控措施数量。
     */
    private Long measureCount;

    /**
     * 活跃隐患数量。
     */
    private Long hiddenDangerCount;

    /**
     * 单元下的风险对象列表；详情接口返回。
     */
    private List<RiskObjectVO> objects;
}
