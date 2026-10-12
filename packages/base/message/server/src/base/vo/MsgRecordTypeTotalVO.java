package com.guhecloud.cube.base.message2.server.base.vo;

import lombok.Data;

/**
 * 消息类型分组统计
 *
 * @author lanchun
 * @date 2021/12/13 10:39
 */
@Data
public class MsgRecordTypeTotalVO {

    /**
     * 数量
     */
    private Integer total;

    /**
     * 消息类型
     */
    private String msgType;
}
