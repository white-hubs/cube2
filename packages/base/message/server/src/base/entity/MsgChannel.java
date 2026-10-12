package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息通道数据模型
 *
 * @author lanchun
 * @date 2021/11/9 17:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MsgChannel implements Entity<Long> {

    private Long id;

    /**
     * 通道名称
     */
    @Unique(message = "通道名称已存在")
    private String name;

    /**
     * 通道编号
     */
    @Unique(message = "通道编号已存在")
    private String code;
}
