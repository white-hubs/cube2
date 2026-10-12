package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人员证书。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_employee_cert")
public class EmployeeCert extends BasicEnhancedTenantEntity<Long> {

    /**
     * 人员证书ID。
     */
    private Long id;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名快照。
     */
    private String userName;

    /**
     * 证书分类ID。
     */
    private Long categoryId;

    /**
     * 证书类型ID。
     */
    private Long typeId;

    /**
     * 证书名称。
     */
    private String certName;

    /**
     * 证书编号。
     */
    private String certNo;

    /**
     * 发证机构。
     */
    private String issueOrg;

    /**
     * 发证日期。
     */
    private LocalDate issueDate;

    /**
     * 有效期开始日期。
     */
    private LocalDate validStart;

    /**
     * 有效期结束日期。
     */
    private LocalDate validEnd;

    /**
     * 有效期数值快照。
     */
    private Integer validPeriodSnapshot;

    /**
     * 有效期单位快照，DAY/MONTH/YEAR。
     */
    private String validPeriodUnitSnapshot;

    /**
     * 到期提前提醒天数快照。
     */
    private Integer warnDaysSnapshot;

    /**
     * 是否需要复审。
     */
    private Boolean needReview;

    /**
     * 复审周期数值快照。
     */
    private Integer reviewPeriodSnapshot;

    /**
     * 复审周期单位快照，DAY/MONTH/YEAR。
     */
    private String reviewPeriodUnitSnapshot;

    /**
     * 复审状态，由编辑表单手动维护。
     */
    private String reviewStatus;

    /**
     * 附件文件ID。
     */
    private Long attachmentFileId;

    /**
     * 证书状态，VALID/LEFT/DISABLED等。
     */
    private String certStatus;

    /**
     * 是否已发送到期提醒。
     */
    private Boolean warnNotified;

    /**
     * 到期提醒发送时间。
     */
    private LocalDateTime warnNotifyTime;
}
