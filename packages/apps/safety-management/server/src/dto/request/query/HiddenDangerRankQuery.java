package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 隐患排名统计参数
 *
 * @author lanchun
 * @date 2024/4/8 11:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerRankQuery extends BaseQuery {

    /**
     * 处理数量:HIDDEN_HANDLER,未处理数量:HIDDEN_UN_HANDLER
     */
    private String stateRange;
}
