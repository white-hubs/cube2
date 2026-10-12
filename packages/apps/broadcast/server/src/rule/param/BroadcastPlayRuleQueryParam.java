package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class BroadcastPlayRuleQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
