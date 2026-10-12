package com.guhecloud.cube.srv.gen.personnel.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/20 11:38
 * @Description: 人员信息扩展表
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("staff_extend")
@EqualsAndHashCode(callSuper = false)
public class StaffExtendPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 员工id
     */
    private Long id;
    /**
     * 短号
     */
    private String cornet;
}
