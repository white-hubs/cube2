package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 隐患分类统计
 * @date 2024/1/24 10:42
 */
@Data
public class HiddenDangerTypeStatisticsVO {
    /**
     * 隐患单总数
     */
    private Long totalNum;
    /**
     * 隐患分类统计
     */
    List<HiddenDangerTypeStatistics> typeStatisticsList;

}
