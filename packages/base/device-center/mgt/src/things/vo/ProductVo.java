package com.guhecloud.cube.iot.mgt.things.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import lombok.Data;

import java.util.List;

@Data
public class ProductVo {
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
     * 是否自定义品类<br/>
     * 1:是<br/>
     * 0:否
     */
    private Integer isCustom;

    /**
     * 产品品类ID
     */
    private Long prodCategoryId;

    private DictItemVo prodCategoryItem;

    /**
     * 产品品类名称
     */
    private String prodCategoryName;

    /**
     * 产品品类名称(包含上级)
     */
    private String prodCategoryNameWithCascade;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    private Long nodeType;

    private String nodeTypeName;

    /**
     * 产品品牌id
     */
    private Long prodBrandId;

    private DictItemVo prodBrandItem;

    /**
     * 产品品牌名称
     */
    private String prodBrandName;

    /**
     * 产品型号名称
     */
    private String prodModel;

    /**
     * 接入方式Id
     */
    private Long accessTypeId;

    private DictItemVo accessTypeItem;

    /**
     * 接入方式名称
     */
    private String accessTypeName;

    /**
     * 消息协议ID（非字典表）
     */
    private Long protocolId;

    /**
     * 消息协议名称
     */
    private String protocolName;

    /**
     * 产品图标
     */
    private Long prodIcon;

    /**
     * 产品图片
     */
    private Long prodPic;

    private FileVO prodIconFile;

    private FileVO prodPicFile;

    /**
     * 产品描述
     */
    private String remark;

    /**
     * 产品状态: 0启用 1禁用
     */
    private Integer status;

    /**
     * 产品状态名称
     */
    private String statusName;

    /**
     * 产品扩展字段,更多描述的可能
     */
    private String extendConfig;

    /**
     * 使用该产品的设备数量
     */
    private long deviceCount;

    private String createTimeFormat;

    private String updateTimeFormat;

    private long adminId;

    private List<ProductPropVo> productPropList;

    private List<ProductEventVo> productEventList;

    private List<ProductMethodMgtVo> productMethodList;
}
