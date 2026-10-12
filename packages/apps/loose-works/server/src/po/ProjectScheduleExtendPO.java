package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/21 15:26
 * @Description: 工程监管扩展表
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_schedule_extend")
public class ProjectScheduleExtendPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主表id
     */
    private Long id;
    /**
     * 施工方联系方式
     */
    private String partyBMobile;

    /**
     * 档位号
     */
    private String stallCode;

    /**
     * 档位联系人
     */

    private String stallLiaison;

    /**
     * 档位联系方式
     */
    private String stallMobile;

    /**
     * 安全对接人
     */
    private String safetyDockUserId;

    /**
     * 建设方
     */
    private String constructB;
}
