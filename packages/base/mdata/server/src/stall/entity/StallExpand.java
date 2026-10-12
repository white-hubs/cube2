package com.guhecloud.cube.biz.mdata.server.stall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 档位扩展信息
 */
@Data
@TableName(value = "stall_expand")
public class StallExpand extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 档位主键
     */
    private Long stallId;

    /**
     * 经纬度信息
     */
    private String lntAndLat;

    /**
     * 位置描述信息
     */
    private String locationDes;
}
