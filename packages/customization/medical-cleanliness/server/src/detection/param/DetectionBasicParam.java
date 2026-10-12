package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DetectionBasicParam {
    private Long id;
    /**
     * 依据名称
     */
    @NotBlank(message = "依据名称不能为空")
    @JsonProperty("name")
    private String basicName;
    /**
     * 标准列表
     */
    private List<StandardParam> standards;

    @Data
    public static class StandardParam {
        private Long id;
        /**
         * 标准名称
         */
        private String name;
        /**
         * 标准属性列表
         */
        private List<PropertyParam> properties;
    }

    @Data
    public static class PropertyParam {
        private Long id;
        /**
         * 产品id
         */
        private Long productId;
        /**
         * 产品名称
         */
        private String productName;
        /**
         * 产品属性名称
         */
        @JsonProperty("propertyName")
        private String productPropertyName;
        /**
         * 产品属性code
         */
        @JsonProperty("propertyCode")
        private String productPropertyCode;
        /**
         * 产品属性单位
         */
        @JsonProperty("propertyUnit")
        private String productPropertyUnit;
        /**
         * 属性租户id
         */
        @JsonProperty("tenantId")
        private Integer productPropertyTenant;

        /**
         * 阈值上限
         */
        private BigDecimal maxVal;
        /**
         * 阈值下限
         */
        private BigDecimal minVal;
    }

}
