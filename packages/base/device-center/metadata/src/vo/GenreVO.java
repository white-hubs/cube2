package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GenreVO {

    private Long genre;
    //类型
    private String genreName;
    //数量
    private Integer num;
    private BigDecimal proportion;

    private String icon;
}
