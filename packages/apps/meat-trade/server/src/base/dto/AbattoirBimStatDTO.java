package com.guhecloud.cube.biz.meattrade.server.base.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lzy
 * @date 2022/4/7
 */
@Data
public class AbattoirBimStatDTO {

    /**
     * 屠宰场主键
     */
    private Long id;

    /**
     * 屠宰场名称
     */
    private String name;

    /**
     * 片数
     */
    private Integer slices;

    /**
     * 重量
     */
    private BigDecimal weight;

}
