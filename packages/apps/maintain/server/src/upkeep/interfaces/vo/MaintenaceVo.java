package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.Data;

@Data
public class MaintenaceVo {
    private Long id;
    private String name;
    private String status;
    private String statusName;
    private Integer tenant;
    private Long objId;
}
