package com.guhecloud.cube.biz.todo.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待办业务类型统计返回参数
 *
 * @author lanchun
 * @date 2022/7/22 11:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoBusinessTypeStatisticsVO {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 数量
     */
    private Long total;
}
