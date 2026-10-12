package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

/**
 * 设备事件附加信息
 */
@Data
public class DeviceEventAdditionalMsg {
    private long deviceId;
    private String eventInstanceId;
    private String eventCode;
    /**
     * json Object<br/>
     * key ==> additional_with_pic_file_des 图片描述<br/>
     */
    private String additional;
    private long eventTime;
}
