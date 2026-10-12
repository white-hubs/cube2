package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 场景规则查询参数
 *
 * @author lanchun
 * @date 2022/6/30 10:31
 */
@Data
public class RuleSceneQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
