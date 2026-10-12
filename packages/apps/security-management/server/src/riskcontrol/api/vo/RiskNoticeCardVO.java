package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskNoticeCardVO {
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
     * 区域所属部门 ID；告知卡按责任部门筛选时使用区域部门。
     */
    private String departmentId;

    /**
     * 区域所属部门引用对象，由 departmentId 解析得到。
     */
    private RiskDepartmentRefVO department;

    /**
     * 风险单元责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 风险单元责任人引用对象，由 responsibleUserId 解析得到。
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
     * 告知卡警示标志列表。
     */
    private List<RiskWarningSignVO> warningSigns;

    /**
     * 告知卡风险对象列表，包含对象下的管控措施。
     */
    private List<RiskObjectVO> objects;
}
