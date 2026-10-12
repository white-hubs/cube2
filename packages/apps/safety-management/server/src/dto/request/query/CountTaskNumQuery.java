package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 任务数统计查询参数
 * @date 2023/9/14 11:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CountTaskNumQuery extends BaseQuery {
    /**
     * 任务状态
     */
    private List<String> status;

}
