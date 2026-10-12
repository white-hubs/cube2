package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/18 19:04
 * @Description:
 */

@Data
public class FieldConfigurationVO {

    /**
     * 主键
     */
    private Long id;
    private String fieldName;
    private String fieldCode;
    private Boolean required;
    /**
     * 是否隐藏
     */
    private Boolean hidden;
}
