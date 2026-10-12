package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.CommunicationStatus;
import lombok.Data;

@Data
public class DeviceState {

    /**
     * 设备状态<br/>
     * {@link DeviceStatusEnum}
     */
    private Integer status;

    /**
     * 设备最后一次在线时间<br/>
     */
    private Long lastOnlineTime;

    /**
     * 状态是否发生过变化<br/>
     */
    private int statusHasOnceChange;

    /**
     * 设备通信状态<br/>
     * {@link CommunicationStatus}
     */
    private Integer communicationStatus;

    /**
     * 设备最后一次通信时间<br/>
     */
    private Long lastCommunicationOnlineTime;

    /**
     * 三方设备ID<br/>
     */
    private String rawDeviceId;

    /**
     * 设备是否在线<br/>
     */
    public boolean statusIsOnline() {
        return status != null && status == DeviceStatusEnum.ON_LINE.value;
    }

    /**
     * 设备通信状态是否正常<br/>
     */
    public boolean communicationStatusIsNormal() {
        return communicationStatus != null && communicationStatus == DeviceStatusEnum.ON_LINE.value;
    }

    /**
     * 设备状态<br/>
     */
    public String formatStatus() {
        if (status == null) {
            return null;
        }

        DeviceStatusEnum statusEnum = DeviceStatusEnum.valueOf(status);
        return statusEnum.name;
    }
}
