package com.guhecloud.cube.base.iot.product.model.v3.param;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 产品保存参数
 */
@Data
public class ProductSaveParam {

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100")
    private String prodName;

    /**
     * 产品品类ID
     */
    @NotNull(message = "产品品类不能为空")
    private Long prodCategoryId;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    @NotNull(message = "节点类型不能为空")
    @Min(value = 1, message = "节点类型必须为1-3")
    @Max(value = 3, message = "节点类型必须为1-3")
    private Integer nodeType;

    /**
     * 产品品牌ID
     */
    private Long prodBrandId;

    /**
     * 产品型号
     */
    @Size(max = 100, message = "产品型号长度不能超过100")
    private String prodModel;

    /**
     * 消息协议ID
     */
    @NotNull(message = "消息协议不能为空")
    private Long protocolId;

    /**
     * 产品描述
     */
    @Size(max = 500, message = "产品描述长度不能超过500")
    private String remark;

    /**
     * 产品扩展字段
     */
    private String extendConfig;

    /**
     * 产品状态: 1正常 0禁用
     */
    private Integer status;

    /**
     * 产品版本号
     */
    @Size(max = 50, message = "产品版本号长度不能超过50")
    private String prodVersion;

    /**
     * 产品图标
     */
    private Long prodIcon;

    /**
     * 产品图片
     */
    private Long prodPic;
}
