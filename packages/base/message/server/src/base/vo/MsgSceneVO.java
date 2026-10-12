package com.guhecloud.cube.base.message2.server.base.vo;

import com.guhecloud.platform.database.annotation.Unique;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局消息场景数据模型
 *
 * @author lanchun
 * @date 2023/3/7 9:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MsgSceneVO {

    private Long id;

    /**
     * 场景编号
     */
    @Unique(composites = { "applicationCode" }, message = "同一应用下已存在相同场景编号")
    private String code;

    /**
     * 场景名称
     */
    private String name;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
