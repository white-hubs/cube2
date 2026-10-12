package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyVO {
    private Long id;
    /**
     * 产品id。
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 属性名称。
     */
    private String productPropertyName;

    /**
     * 产品属性code。
     */
    private String productPropertyCode;

    /**
     * 产品属性单位
     */
    private String productPropertyUnit;

    /**
     * 产品属性租户id。
     */
    private Integer productPropertyTenant;

    /**
     * 检测依据id。
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long basicId;

    /**
     * 阈值上限。
     */
    private BigDecimal maxVal;

    /**
     * 阈值下限。
     */
    private BigDecimal minVal;
}
