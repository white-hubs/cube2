package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("security_device_auth")
public class SecurityDeviceAuth extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 设备id
     */
    private Long securityDeviceId;
}
