package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息发布屏与开关屏配置管理 关联实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("info_screen_schedule_assoc")
public class InfoScreenScheduleAssocEntity extends BasicEnhancedTenantEntity<Long> {
    /**
     * 关联主键id
     */
    private Long id;
    /**
     * 开关屏配置管理主键id
     */
    private Long informationPlanId;
    /**
     * 基础设备id
     */
    private Long securityDeviceId;
}
