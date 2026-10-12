package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 20:47
 * @Description:
 */

@Data
public class ProjectReformAnalysisVO {
    /**
     * 工程整改总数
     */
    private Long reformNum = 0L;

    /**
     * 环比率
     */
    private float reformChainRatio = 0;

    /**
     * 各市场整改数据排名
     */
    private List<ProjectReformOrderVO> reformRankList;
}
