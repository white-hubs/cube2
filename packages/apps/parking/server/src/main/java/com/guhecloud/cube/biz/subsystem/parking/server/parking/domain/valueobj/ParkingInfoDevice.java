package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingInfoDevice {

    private Long deviceId;

    @Setter
    private Integer currentCarNumber;

    public ParkingInfoDevice(Long deviceId) {
        this.deviceId = deviceId;
    }

}
