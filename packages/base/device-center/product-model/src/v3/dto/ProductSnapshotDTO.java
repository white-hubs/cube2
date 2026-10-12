package com.guhecloud.cube.base.iot.product.model.v3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品快照DTO
 * 用于物模型版本管理和导入导出的产品数据结构
 *
 * <p>JSON字段名使用下划线命名(如prod_number),与数据库字段保持一致</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品编号(唯一标识)
     */
    @JsonProperty("prod_number")
    private String prodNumber;

    /**
     * 产品名称
     */
    @JsonProperty("prod_name")
    private String prodName;

    /**
     * 产品名称拼音
     */
    @JsonProperty("prod_name_pinyin")
    private String prodNamePinyin;

    /**
     * 产品分类名称链
     */
    @JsonProperty("prod_category_name_chain")
    private String prodCategoryNameChain;

    /**
     * 节点类型(1:直连设备, 2:网关设备, 3:网关子设备)
     */
    @JsonProperty("node_type")
    private Integer nodeType;

    /**
     * 品牌编码
     */
    @JsonProperty("prod_brand_name")
    private String prodBrandName;

    /**
     * 产品型号
     */
    @JsonProperty("prod_model")
    private String prodModel;

    /**
     * 协议编码
     */
    @JsonProperty("protocol_code")
    private String protocolCode;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 扩展配置(JSON格式)
     */
    @JsonProperty("extend_config")
    private String extendConfig;

    /**
     * 状态(0:禁用, 1:启用)
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 产品版本
     */
    @JsonProperty("prod_version")
    private String prodVersion;

    /**
     * 产品图标ID
     */
    @JsonProperty("prod_icon")
    private Long prodIcon;

    /**
     * 产品图片ID
     */
    @JsonProperty("prod_pic")
    private Long prodPic;

    /**
     * 创建时间(可选,仅在includeSystemFields=true时导出)
     */
    @JsonProperty("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间(可选,仅在includeSystemFields=true时导出)
     */
    @JsonProperty("updateTime")
    private LocalDateTime updateTime;
}
