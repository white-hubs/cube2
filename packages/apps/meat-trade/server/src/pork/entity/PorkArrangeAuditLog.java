package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 理货审核记录
 *
 * @author lzy
 * @date 2021/9/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName(value = "meattrade_pork_arrange_audit_log")
public class PorkArrangeAuditLog extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 理货审核员主键
     */
    private Long auditorId;

    /**
     * 理货审核主键
     */
    private Long porkArrangeAuditId;

    /**
     * 审核状态
     */
    private PorkArrangeAuditEnum status;

    /**
     * 审核时间
     */
    private LocalDateTime time;

    /**
     * 备注
     */
    private String remark;
}
