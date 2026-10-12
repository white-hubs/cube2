package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeyPersonPageVO {
    /**
     * 重点人员主键。
     */
    private Long id;

    /**
     * 重点人员编号。
     */
    private String personCode;

    /**
     * 姓名。
     */
    private String name;

    /**
     * 手机号。是否脱敏由字典配置控制。
     */
    private String phone;

    /**
     * mdata 区域主键。
     */
    private Long areaId;

    /**
     * 区域名称快照。
     */
    private String areaName;

    /**
     * mdata 区域 sequence。
     */
    private String areaSequence;

    /**
     * 风险等级：HIGH、MEDIUM、LOW。
     */
    private String riskLevel;

    /**
     * 风险等级名称。
     */
    private String riskLevelName;

    /**
     * 监管人员 ouaa 用户主键。
     */
    private Long supervisorUserId;

    /**
     * 监管人员姓名快照。
     */
    private String supervisorName;

    /**
     * 监管原因。
     */
    private String supervisionReason;

    /**
     * 人员状态：ACTIVE 在管，INACTIVE 撤管。
     */
    private String status;

    /**
     * 人员状态名称。
     */
    private String statusName;

    /**
     * 创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updateTime;
}
