package com.guhecloud.cube.base.iot.product.model.v3.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品视图对象
 * 返回给前端的产品数据,使用ID字段进行关联
 */
@Data
public class ProductVO {

    /**
     * 产品ID
     */
    private Long id;

    /**
     * 产品编号
     */
    private String prodNumber;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 产品名称拼音
     */
    private String prodNamePinyin;

    /**
     * 产品品类ID
     */
    private Long prodCategoryId;

    /**
     * 产品品类名称
     */
    private String prodCategoryName;

    /**
     * 产品品类级联名称
     */
    private String prodCategoryNameChain;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    private Integer nodeType;

    /**
     * 产品品牌ID
     */
    private Long prodBrandId;

    /**
     * 产品品牌名称
     */
    private String prodBrandName;

    /**
     * 产品型号
     */
    private String prodModel;

    /**
     * 消息协议ID
     */
    private Long protocolId;

    /**
     * 消息协议名称
     */
    private String protocolName;

    /**
     * 产品描述
     */
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
    private String prodVersion;

    /**
     * 产品图标
     */
    private Long prodIcon;

    /**
     * 产品图片
     */
    private Long prodPic;

    /**
     * 产品图标文件
     */
    private FileVO iconFileVO;

    /**
     * 产品图片文件
     */
    private FileVO picFileVO;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 属性列表(详情查询时返回)
     */
    private List<ProductPropVO> properties;

    /**
     * 事件列表(详情查询时返回)
     */
    private List<ProductEventVO> events;

    /**
     * 方法列表(详情查询时返回)
     */
    private List<ProductMethodVO> methods;
}
