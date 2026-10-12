package com.guhecloud.cube.iot.mgt.things.vo;

import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import lombok.Data;

@Data
public class ProductEventVo {
    /**
     * 产品事件id
     */
    private Long id;

    /**
     * 产品id
     */
    private Long prodId;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件配置<br/>
     * 废弃，占个坑位做兼容
     */
    private String config;

    /**
     * 事件配置
     */
    private EventConfig eventConfig;

    /**
     * 事件类别
     */
    private Long categoryId;

    private DictItemVo categoryItem;

    /**
     * 事件类别名称
     */
    private String categoryName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态: 1 启用 0禁用
     */
    private Integer status;

    private String statusName;
}