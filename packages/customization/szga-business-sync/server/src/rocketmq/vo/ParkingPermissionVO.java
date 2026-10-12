package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/13 16:44
 * @Description:
 */
@Data
public class ParkingPermissionVO {

    private Boolean parkingControl;
    private List<ParkingControlRow> parkingControlRows;

    @Data
    public static class ParkingControlRow {
        /**
         * 业务表主键id
         */
        private String id;
        /**
         * 停车场编码
         */
        private String code;

        /**
         * 停车场名称
         */
        private String name;

    }
}