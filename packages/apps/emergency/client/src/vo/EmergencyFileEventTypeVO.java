package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

@Data
public class EmergencyFileEventTypeVO {

    /**
     * 预案文件数
     */
    private Long totalNum;
    /**
     * 预案事件编码
     */
    private String emergencyEventTypeCode;

    /**
     * 预案事件名称
     */
    private String emergencyEventTypeName;

}
