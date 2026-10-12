package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品定义v3版本
 * 对应表: iot_md_product_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_v3")
@Data
public class MdProduct extends BasicTenantEntity<Long> {

    /**
     * 产品ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品编号(业务编码)
     */
    @TableField(value = "prod_number")
    private String prodNumber;

    /**
     * 产品名称
     */
    @TableField(value = "prod_name")
    private String prodName;

    /**
     * 产品名称拼音首字母(用于检索)
     */
    @TableField(value = "prod_name_pinyin")
    private String prodNamePinyin;

    /**
     * 产品品类ID(内部使用,查询时通过code转换)
     */
    @TableField(value = "prod_category_id")
    private Long prodCategoryId;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     * 使用 {@link com.guhecloud.cube.base.iot.product.model.v3.enums.NodeTypeEnum}
     */
    @TableField(value = "node_type")
    private Integer nodeType;

    /**
     * 产品品牌ID(内部使用,查询时通过code转换)
     */
    @TableField(value = "prod_brand_id")
    private Long prodBrandId;

    /**
     * 产品型号
     */
    @TableField(value = "prod_model")
    private String prodModel;

    /**
     * 消息协议ID(内部使用,查询时通过code转换)
     */
    @TableField(value = "protocol_id")
    private Long protocolId;

    /**
     * 产品描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 产品扩展字段,更多描述的可能
     */
    @TableField(value = "extend_config")
    private String extendConfig;

    /**
     * 产品状态: 1正常 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 产品版本号
     */
    @TableField(value = "prod_version")
    private String prodVersion;

    /**
     * 产品图标(文件ID)
     */
    @TableField(value = "prod_icon")
    private Long prodIcon;

    /**
     * 产品图片(文件ID)
     */
    @TableField(value = "prod_pic")
    private Long prodPic;
}
