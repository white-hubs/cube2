package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.Data;

/**
 * @author lzy
 * @date 2022/5/4
 */
@Data
public class FaultTypeVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
    private String applicationCode;
}
