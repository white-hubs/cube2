package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 20:08
 * @Description:
 */

@Data
public class UpdateStaffMessage {
    public static final String TOPIC = "UPDATE_STAFF_MESSAGE";
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 组织id
     */
    private Long organizationId;

    /**
     * 员工状态
     */
    private String status;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 短号
     */
    private String cornet;
    /**
     * 身份证号
     */
    private String idCard;
}
