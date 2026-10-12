package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 19:09
 * @Description: 入职审批表单数据
 */

@Data
public class InductionPO {

    /**
     * 入职部门
     */
    private List<Long> organizationIdList;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 性别
     */
    private String gender;

    /**
     * 服务期限
     */
    private LocalDateTime servicePeriod;

    /**
     * 权限信息
     */
    private List<UserPermission> userPermissionList;

    /**
     * 停车权限
     */
    private ParkingPermissionVO parkingPm;

    /**
     * 门禁权限
     */
    private DoorPermissionVO doorPm;

    /**
     * 消费权限
     */
    private Boolean consumePm;

    @Data
    public static class UserPermission {
        /**
         * 停车权限
         */
        private ParkingPermissionVO parkingPm;
        /**
         * 门禁权限
         */
        private DoorPermissionVO doorPm;
        /**
         * 消费权限
         */
        private Boolean consumePm;
    }
}
