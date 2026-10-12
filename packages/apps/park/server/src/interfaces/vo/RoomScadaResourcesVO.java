package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

@Data
public class RoomScadaResourcesVO extends BasicTenantEntity<Long> {

    /**
     * ID
     */
    private Long id;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 二维组态地址
     */
    private String url;

}
