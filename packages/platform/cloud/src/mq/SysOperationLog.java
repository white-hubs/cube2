package com.guhecloud.platform.cloud.mq;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-21  14:17
 * @Description:
 * @Version: 1.0
 */
@Data
public class SysOperationLog {
    /**
     * 请求用户id
     */
    private Long userId;
    /**
     * 请求用户名称
     */
    private String userName;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求状态码
     */
    private String statusCode;
    /**
     * 请求ip
     */
    private String ipAddress;
    /**
     * 请求参数
     */
    private String parameters;
    /**
     * 异常信息
     */
    private String abnormalInfo;
    /**
     * 请求结果
     */
    private String result;
    /**
     * 租户
     */
    private Integer tenant;
    /**
     * 请求时间
     */
    private LocalDateTime createTime;
}
