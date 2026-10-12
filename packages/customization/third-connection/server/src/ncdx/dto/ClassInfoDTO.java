package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.dto;

import lombok.Data;

/**
 * 南充党校教务三方对象
 */
@Data
public class ClassInfoDTO {
    /**
     * id
     */
    private String teas_jwgl_bjgl_id;
    /**
     * 名称
     */
    private String name;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 年份
     */
    private Integer school_year;
    /**
     * 班次类型
     */
    private String bclx_text;
}
