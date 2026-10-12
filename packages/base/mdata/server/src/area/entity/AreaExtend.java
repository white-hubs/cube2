package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class AreaExtend extends BasicEnhancedTenantEntity<Long> {

    /**
     * 区域主键
     */
    private Long id;

    /**
     * 区域介绍
     */
    private String introduce;

    /**
     * 图片附件集合
     */
    private String pictureFileIds;

    /**
     * 视频附件集合
     */
    private String videoFileIds;
}
