package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 企业走访
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("enterprise_interview")
public class EnterpriseInterviewPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 走访时间
     */
    private LocalDateTime time;

    /**
     * 走访类型ID
     */
    private Long interviewTypeId;

    /**
     * 走访人员
     */
    private String person;

    /**
     * 企业接待人员
     */
    private String receptionPerson;

    /**
     * 走访记录
     */
    private String recordContent;

    /**
     * 关联诉求ID
     */
    private String appealIds;

}
