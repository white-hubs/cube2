package com.guhecloud.cube.biz.linkage.server.linkage.param;

import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionBeforeParam {

    /**
     * 前置条件类型
     */
    @NotNull(message = "动作前置条件")
    protected ActionBeforeTypeEnum actionBeforeType;

    /**
     * 前置配置
     */
    private Object actionBeforeConfig;

    /**
     * 启用状态
     */
    protected Boolean enabled;

}
