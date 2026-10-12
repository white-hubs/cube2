package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import lombok.Data;

/**
 * 联动执行状态
 */
@Data
public class ActionStatus {
    private String devId;
    private String propCode;
    private Object popValue;
    /**
     * 枚举值:
     * INIT: 初始状态，尚未进行操作
     * PROCESSING: 在进行本地处理（这是一种中间状态，避免重复处理）
     * ACCEPT: 命令已被接收（远程处理是异步的情况下，远端通知命令已被接收）
     * SUCCESS: 命令已处理成功
     * FAIL: 命令已处理失败
     * EXPIRED: PROCESSING状态下未及时收到远端确认，或等待远程确认命令执行结果超时
     */
    private String execStatus;
    private Long execTs;
    private Long completeTs;
}
