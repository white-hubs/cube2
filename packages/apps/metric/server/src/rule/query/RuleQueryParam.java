package com.guhecloud.cube.biz.metric.server.rule.query;

import com.guhecloud.cube.biz.metric.server.rule.enumeration.RuleItemTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RuleQueryParam extends Paging {

    /**
     * 项类型
     */
    private RuleItemTypeEnum itemType;
}
