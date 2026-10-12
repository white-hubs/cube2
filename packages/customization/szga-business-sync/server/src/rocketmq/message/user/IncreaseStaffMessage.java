package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 20:41
 * @Description: 人事管理操作新增数据
 */

@Data
public class IncreaseStaffMessage {
    private Long employeeId;
    private String name;
    private String mobile;
    private Long organizationId;
    private String status;
    private Integer tenant;
    private String idCard;
    private String cornet;
    private String gender;
    public static final String TOPIC = "INCREASE_STAFF";
}
