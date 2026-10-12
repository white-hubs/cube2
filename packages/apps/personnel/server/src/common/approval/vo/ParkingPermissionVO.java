package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/19 11:12
 * @Description:
 */
@Data
public class ParkingPermissionVO {

    private String parkingControl;
    private List<ParkingControlRow> parkingControlRows;

    @Data
    public static class ParkingControlRow {
        private String deviceId;
        private String deviceName;
        private String code;
    }
}
