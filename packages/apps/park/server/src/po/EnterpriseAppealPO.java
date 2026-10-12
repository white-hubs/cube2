package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 企业诉求
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("enterprise_appeal")
public class EnterpriseAppealPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 诉求类型ID
     */
    private Long appealTypeId;

    /**
     * 诉求时间
     */
    private LocalDateTime time;

    /**
     * 反馈人员ID
     */
    private Long userId;

    /**
     * 反馈人员名
     */
    private String username;

    /**
     * 反馈人员昵称
     */
    private String nickname;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 描述
     */
    private String description;

    /**
     * 解决措施
     */
    private String measure;

}
