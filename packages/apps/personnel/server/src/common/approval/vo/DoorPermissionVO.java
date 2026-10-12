package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/19 11:12
 * @Description:
 */
@Data
public class DoorPermissionVO {

    private String accessControl;
    private List<AccessControlRow> accessControlRows;

    @Data
    public static class AccessControlRow {
        private String id;
        private String name;
        private String doorCode;
        private String doorStatus;
        private String businessId;
    }
}