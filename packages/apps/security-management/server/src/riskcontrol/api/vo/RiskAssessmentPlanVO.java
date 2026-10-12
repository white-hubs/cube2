package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskAssessmentPlanVO {
    /**
     * 评估计划 ID。
     */
    private Long id;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险单元引用对象，由 unitId 解析得到；code=单元编号，name=单元名称。
     */
    private RiskOptionVO unit;

    /**
     * 评估计划名称。
     */
    private String planName;

    /**
     * 周期类型。
     */
    private String cycleType;

    /**
     * 下次执行时间。
     */
    private LocalDateTime nextExecuteTime;

    /**
     * 最近一次触发执行时间。
     */
    private LocalDateTime lastExecuteTime;

    /**
     * 计划状态，取值：ENABLED=启用，DISABLED=禁用。
     */
    private String status;

    /**
     * 通知人员用户 ID 集合。
     */
    private List<String> notifyUserIds;

    /**
     * 通知人员引用对象集合，由 notifyUserIds 解析得到。
     */
    private List<RiskUserRefVO> notifyUsers;
}
