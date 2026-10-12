package com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.command;

import lombok.Getter;

@Getter
public class ScadaResourcesCommandModel {

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
