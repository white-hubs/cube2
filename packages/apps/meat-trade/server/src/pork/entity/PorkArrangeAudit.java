package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 理货数据审核
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "meattrade_pork_arrange_audit")
public class PorkArrangeAudit extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 来货报备批次号
     */
    private String porkReportCode;

    /**
     * 检疫票登记主键
     */
    private Long pratiqueRegisterId;

    /**
     * 食安检验主键
     */
    private Long spotCheckId;

    /**
     * 理货数据主键
     */
    private Long porkArrangeId;

    /**
     * 审核员主键
     */
    private Long auditorId;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核状态
     */
    private PorkArrangeAuditEnum auditStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 市场方租户编号
     */
    private Integer tenant;

}
