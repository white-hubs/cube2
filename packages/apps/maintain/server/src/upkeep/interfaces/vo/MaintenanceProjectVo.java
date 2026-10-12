package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.Data;

@Data
public class MaintenanceProjectVo {
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
     * 维保项编码
     */
    private String code;
    /**
     * 内容
     */
    private String maintenanceContent;
}
