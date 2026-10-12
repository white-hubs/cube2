package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 定时规则查询条件
 *
 * @author lanchun
 * @date 2022/6/29 18:02
 */
@Data
public class RuleTimingQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 周期类型false单次,true循环
     */
    private Boolean looped;
}
