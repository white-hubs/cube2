package com.guhecloud.cube.base.message2.server.base.vo;

import lombok.Data;

/**
 * 消息通道详情对象
 *
 * @author lanchun
 * @date 2021/11/10 13:54
 */
@Data
public class MsgChannelVO {

    private Long id;

    /**
     * 通道名称
     */
    private String name;

    /**
     * 通道编号
     */
    private String code;
}
