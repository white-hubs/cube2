package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DetectionBasicDetailVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private List<StandardVO> standards;
    @Data
    public static class StandardVO {
        private Long id;
        private String name;
        private List<PropertyVO> properties;
    }
    @Data
    public static class PropertyVO {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
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
