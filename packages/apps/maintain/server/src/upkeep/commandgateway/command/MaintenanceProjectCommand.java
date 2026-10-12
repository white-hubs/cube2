package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

@Data
public class MaintenanceProjectCommand {
    private Long id;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 维保项内容
     */
    private String maintenanceContent;
    /**
     * 是否删除
     */
    private Boolean deleted;
}
