package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 通用统计
 * @date 2024-01-26 10:08:36
 */
@Data
public class CommonStatVO {
    private Long id;
    private String name;
    private String value;
    private String type;
}