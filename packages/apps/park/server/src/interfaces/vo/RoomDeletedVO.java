package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

@Data
public class RoomDeletedVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    private Boolean deleted;

}
