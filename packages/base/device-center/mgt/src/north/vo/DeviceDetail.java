package com.guhecloud.cube.iot.mgt.north.vo;

import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDetail {
    private String deviceId;
    private String deviceParentId;
    private String deviceNumber;
    private String productId;
    private String productName;
    private String deviceName;
    private Integer onlineState;
    private String tags;

    private List<ProductPropVo> productPropVoList;
    private List<ProductEventVo> productEventVoList;
    private List<KeyValueWithTs> snapshots;
    private List<Object> productCommandVoList;
    private DeviceExtConfig extConfig;
}
