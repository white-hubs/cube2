package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 联动事件数据
 */
@Data
public class LinkageCfgEventChange implements Serializable {

    /**
     * 事件类型<br/>
     * ADD
     * UPDATE
     * DELETE
     */
    private String eventType;

    /**
     * 联动配置ID
     */
    private Long linkageId;

    /**
     * 时间戳
     */
    private Long timestamp;

    @Override
    public String toString() {
        return "LinkageEvent{" +
                "eventType=" + eventType +
                ", linkageId=" + linkageId +
                ", timestamp=" + timestamp +
                '}';
    }
}