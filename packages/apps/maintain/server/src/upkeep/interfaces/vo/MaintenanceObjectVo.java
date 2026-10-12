package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.Data;

import java.util.List;

@Data
public class MaintenanceObjectVo {
    /**
     * id
     */
    private Long id;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 计划id
     */
    private Long planId;

    private Long keyId;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 区域
     */
    private String area;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 安装地址
     */
    private String installationAddress;

    List<MaintenanceProjectVo> objectProjects;
}
