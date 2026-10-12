package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组设备
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupDevice extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long groupId;
    private Long deviceId;

    /**
     * 绑定设备时所在纬度
     */
    private Double latitude;

    /**
     * 绑定设备时所在经度
     */
    private Double longitude;

    /**
     * 区县位置
     */
    private String districtLocation;

    /**
     * 详细位置
     */
    private String locationDetail;
}
