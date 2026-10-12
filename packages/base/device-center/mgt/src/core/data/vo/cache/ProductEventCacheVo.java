package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import lombok.Data;

@Data
public class ProductEventCacheVo {
    private long eventId;

    /**
     * 事件code
     */
    private String eventCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件分类code
     */
    private String categoryCode;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 产品属性code
     */
    private String propCode;

    /**
     * 对比条件
     */
    private String compareType;

    /**
     * 注意:内部实际均有String进行表述<br/>
     * 对比参数，值从小到大，有序排列
     */
    private Object[] compareValue;

    /**
     * 仅仅进行数据库查询时使用<br/>
     * 其他地方忽略该字段
     */
    private String compareValueStr;

    /**
     * 说明字符串
     */
    private String transparentStatement;

    /**
     * 延时时间
     */
    private Long delayTime;
}
