package com.guhecloud.cube.biz.mdata.server.area.param;

import lombok.Data;

@Data
public class AreaLinkTagParam {
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 标签ids
     */
    private String tagIds;
}
