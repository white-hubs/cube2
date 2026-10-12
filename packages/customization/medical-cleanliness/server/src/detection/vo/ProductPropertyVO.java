package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ProductPropertyVO {
    /**
     * 产品id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 属性code
     */
    private String propertyCode;
    /**
     * 属性单位
     */
    private String propertyUnit;
    /**
     * 租户Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer tenantId;

}
