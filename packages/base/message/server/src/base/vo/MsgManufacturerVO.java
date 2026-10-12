package com.guhecloud.cube.base.message2.server.base.vo;

import lombok.Data;

/**
 * 消息厂家详情对象
 *
 * @author lanchun
 * @date 2023/3/7 14:08
 */
@Data
public class MsgManufacturerVO {

    private Long id;

    /**
     * 厂家名称
     */
    private String name;

    /**
     * 厂家编号
     */
    private String code;

    /**
     * 账号规则
     */
    private String accountRule;
}
