package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceLogsDTO {

    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 备注
     */
    private String remark;
}
