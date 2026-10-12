package com.guhecloud.cube.base.iot.product.model.v3.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 物模型版本视图对象
 * 用于展示物模型版本快照信息
 */
@Data
public class ProductModelVersionVO {

    /**
     * 版本ID
     */
    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 产品编号
     */
    private String prodNumber;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 版本号(自动递增)
     */
    private Integer versionNumber;

    /**
     * 版本标签(可选,如: v1.0.0)
     */
    private String versionTag;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 完整的物模型JSON快照
     */
    private String modelSnapshot;

    /**
     * 变更类型: CREATE创建, UPDATE更新, ROLLBACK回滚
     */
    private String changeType;

    /**
     * 变更摘要
     */
    private String changeSummary;

    /**
     * 是否当前激活版本: 1是 0否
     */
    private Integer isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人名称
     */
    private String createByName;
}
