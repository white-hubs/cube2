package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import lombok.Data;

@Data
public class ReportTypeAddOrUpdateParam {

    private Long id;
    /**
     * 类型名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;
}
