package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

@Data
public class DeviceAccountVO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 收藏
     */
    private Boolean collected = false;

}
