package com.guhecloud.cube.srv.gen.stall.server.param;

import lombok.Data;

@Data
public class StallExcelTemplate {
    /**
     * 档位编号
     */
    private String code;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 具体位置
     */
    private String location;

}
