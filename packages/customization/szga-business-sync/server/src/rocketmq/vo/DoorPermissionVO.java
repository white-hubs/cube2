package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/13 16:44
 * @Description:
 */
@Data
public class DoorPermissionVO {

    private Boolean accessControl;
    private List<AccessControlRow> accessControlRows;

    @Data
    public static class AccessControlRow {
        /**
         * 业务表主键
         */
        private String id;

        /**
         * 设备id
         */
        private String businessId;
        /**
         * 设备名称
         */
        private String name;

        /**
         * 门禁编码
         */
        private String doorCode;

        /**
         * 状态 （0）
         */
        private String status;

        /**
         * 第三方设备id
         */
        private String outBusinessId;
    }
}
