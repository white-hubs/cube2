package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import lombok.Data;

@Data
public class ConferenceGoodsParam {

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
