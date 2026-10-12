package com.guhecloud.cube.biz.mdata.server.area.vo;

import lombok.Data;

/**
 * 区域组下的区域
 *
 * @author lanchun
 * @date 2022/4/18 14:05
 */
@Data
public class AreaGroupConfigVO {

    private Long id;

    /**
     * 区域组主键
     */
    private Long areaGroupId;

    /**
     * 区域组序列号
     */
    private String areaGroup;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String area;

}
