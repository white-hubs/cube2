package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  11:20
 * @Description:
 * @Version: 1.0
 */
@Data
public class KeyValueDTO {
    private String id;

    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private Integer value;
    /**
     * 百分比
     */
    private BigDecimal percentage;

    public KeyValueDTO() {

    }

    public KeyValueDTO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

}
