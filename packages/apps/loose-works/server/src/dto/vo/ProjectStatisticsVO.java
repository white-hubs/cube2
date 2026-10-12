package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 工程统计
 * @date 2023/9/14 18:53
 */
@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class ProjectStatisticsVO {
    /**
     * 小散工程数
     */
    private Long total;

    /**
     * 环比
     */
    private Float ringRatio;
    /**
     * 请示中
     */
    private Long apply;
    /**
     * 签约中
     */
    private Long signing;
    /**
     * 进行中
     */
    private Long underway;
    /**
     * 待验收
     */
    private Long waitAccept;
    /**
     * 已完成
     */
    private Long completed;

    /**
     * 异常数
     */
    private Long abnormal;

    /**
     * 正常数
     */
    private Long normal;
}
