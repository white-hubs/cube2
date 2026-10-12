package com.guhecloud.cube.biz.metric.server.score.config.dto;

import lombok.Data;

import java.util.List;

/**
 * 安全报告算分配置
 *
 * @author lanchun
 * @date 2025/6/3 15:54
 */
@Data
public class ScoreRules {

    /**
     * 分项列表
     */
    private List<ScoreItems> scoreItems;
}
