package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 小散排名统计参数
 *
 * @author lanchun
 * @date 2024/4/8 11:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LooseWorksRankQuery extends BaseQuery {

    /**
     * 进行中数量:UNDERWAY,异常数量:ABNORMAL
     */
    private String stateRange;
}
