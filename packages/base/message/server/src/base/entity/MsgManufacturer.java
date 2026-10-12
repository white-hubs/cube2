package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息厂家
 *
 * @author lanchun
 * @date 2023/3/7 9:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MsgManufacturer implements Entity<Long> {

    private Long id;

    /**
     * 厂家名称
     */
    @Unique(message = "厂家名称已存在")
    private String name;

    /**
     * 厂家编号
     */
    @Unique(message = "厂家编号已存在")
    private String code;
}
