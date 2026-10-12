package com.guhecloud.cube.srv.gen.maintain.server.scada.interfaces.vo;

import lombok.Data;

@Data
public class ScadaResourcesVO {

    private Long id;

    /**
     * 类别编号
     */
    private String categoryCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 组态地址
     */
    private String url;

    /**
     * 描述
     */
    private String description;

}
