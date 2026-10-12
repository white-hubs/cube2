package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command;

import lombok.Data;

/**
 * 广播播放变量参数
 *
 * @author lanchun
 * @date 2024/3/6 18:50
 */
@Data
public class BroadcastPlayVariableParam {

    /**
     * 变量名
     */
    private String name;

    /**
     * 变量值
     */
    private String value;
}
