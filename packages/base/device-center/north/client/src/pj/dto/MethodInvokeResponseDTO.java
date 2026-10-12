package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.Map;

/**
 * 方法调用响应
 */
@Data
public class MethodInvokeResponseDTO {

    /**
     * 客户端传入的命令序列号
     */
    private String commandSeq;

    /**
     * 命令实例ID(用于后续状态查询)
     */
    private String commandHandle;

    /**
     * 执行状态码
     * - REJECT: 命令被拒绝执行
     * - INIT: 初始状态，尚未进行操作
     * - PROCESSING: 命令处理中
     * - ACCEPT: 命令已被接收
     * - SUCCESS: 命令已处理成功
     * - FAIL: 命令已处理失败
     * - EXPIRED: 命令超时
     */
    private String statusCode;

    /**
     * 执行消息
     */
    private String message;

    /**
     * 输出参数(方法执行返回的结果)
     */
    private Map<String, Object> outputParams;
}
