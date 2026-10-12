package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 会议物品表
 *
 * @TableName conference_goods
 */
@Data
public class ConferenceGoods extends BasicEnhancedTenantEntity<Long> {
    /**
     *
     */
    private Long id;

    /**
     * 物品名称
     */
    private String goodsName;

    /**
     * 说明
     */
    private String illustrate;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 区域名称
     */
    private String areaName;
}