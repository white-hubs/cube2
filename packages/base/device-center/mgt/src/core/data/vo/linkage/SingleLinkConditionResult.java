package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.iot.mgt.core.data.lang.SourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 最细粒度的联动表达
 */
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SingleLinkConditionResult implements Serializable {

    /**
     * 实例id required
     */
    private String instanceId;

    /**
     * 项目id
     */
    private Long tenantId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 事件id required
     */
    private String eventId;
    /**
     * 事件名称 required
     */
    private String eventName;

    /**
     * 事件类型 required
     */
    private String eventType;

    private String propertyConfig;
    /**
     * 对比条件
     */
    private String compareType;
    /**
     * 对比值
     */
    private String compareName;

    /**
     * 对比值
     */
    private Object[] compareValue;

    /**
     * 对比结果
     */
    private boolean compareResult;

    /**
     * 触发时间 required
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date eventTime;

    /**
     * 延时触发时间,eventTime <= delayEventTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date delayEventTime;

    /**
     * 事件产生源 required
     */
    private SourceType source;

    /**
     * 属性 标识符<br/>
     *
     * @deprecated 没有任何用，不再保证此字段一定有值<br/>
     */
    private String identifier;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性名称
     */
    private String propertyCode;

    /**
     * 属性单位
     */
    private String propertyUnit;

    /**
     * 当前值
     */
    private Object value;
}