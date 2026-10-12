package com.guhecloud.cube.base.message2.server.base.rocketmq.message;

import lombok.Data;

@Data
public class MsgRecordTerminalMqMessage {

    private Long id;

    /**
     * 消息记录主键
     */
    private Long msgRecordId;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 终端名称
     */
    private String terminalName;
}
