package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 工程进度统计
 * @date 2023/9/15 9:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectScheduleStatisticsVO {
    /**
     * 进行中
     */
    private Long underway;
    /**
     * 已完成
     */
    private Long completed;
    /**
     * 待验收
     */
    private Long waitAccept;
}
