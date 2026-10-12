package com.guhecloud.cube.biz.mdata.server.bizlog.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-27  17:29
 * @Description:
 * @Version: 1.0
 */
@Data
public class OperationLogVO {
    private Long id;
    /**
     * 请求用户id
     */
    private Long userId;
    /**
     * 请求用户名称
     */
    private String userName;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求状态码
     */
    private String statusCode;
    /**
     * 请求参数
     */
    private String parameters;
    /**
     * 请求结果
     */
    private String result;
    private LocalDateTime createTime;
}
