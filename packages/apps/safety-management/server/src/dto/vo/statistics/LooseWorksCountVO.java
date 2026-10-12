package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 小散工程统计信息
 * @date 2023/9/19 15:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LooseWorksCountVO {
    /**
     * 总数
     */
    private Integer total;
    /**
     * 异常数
     */
    private Integer abnormal;

    /**
     * 已完成数据
     */
    private Integer completedNum;
}
