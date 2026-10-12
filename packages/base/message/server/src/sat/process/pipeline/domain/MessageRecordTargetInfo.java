package com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain;

import lombok.Data;

@Data
public class MessageRecordTargetInfo {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号码
     */
    private String userPhone;

    /**
     * 发送邮箱
     */
    private String userEmail;

    /**
     * 接收者信息
     */
    private String receiver;

    /**
     * 来源租户
     */
    private Integer sourceTenant;
}
