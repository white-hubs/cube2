package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 人员证书变更记录。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_employee_cert_change_log")
public class EmployeeCertChangeLog extends BasicEnhancedTenantEntity<Long> {

    /**
     * 变更记录ID。
     */
    private Long id;

    /**
     * 人员证书ID。
     */
    private Long certId;

    /**
     * 变更类型。
     */
    private String changeType;

    /**
     * 变更内容JSON。
     */
    private String changeContent;

    /**
     * 操作人OUAA用户ID。
     */
    private Long operatorId;

    /**
     * 操作人姓名。
     */
    private String operatorName;

    /**
     * 变更时间。
     */
    private LocalDateTime changeTime;
}
