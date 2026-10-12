package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-22 15:14
 */
@Data
public class CommandResponse {

    /**
     * 事件推送时忽略
     *
     * @date 2022/5/6 16:17
     **/
    private String commandSeq;

    private String commandHandle;

    //REJECT: 命令被拒绝执行
    //NIT: 初始状态，尚未进行操作
    //PROCESSING: 在进行本地处理（这是一种中间状态，避免重复处理）

    //PROCESSING: 在进行本地处理（这是一种中间状态，避免重复处理）
    //ACCEPT: 命令已被接收（远程处理是异步的情况下，远端通知命令已被接收）

    //SUCCESS: 命令已处理成功
    //FAIL: 命令已处理失败
    //    //EXPIRED: PROCESSING状态下未及时收到远端确认，或等待远程确认命令执行结果超时
    private String statusCode;

    private String message;

}
