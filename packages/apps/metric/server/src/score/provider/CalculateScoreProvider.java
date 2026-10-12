package com.guhecloud.cube.biz.metric.server.score.provider;

import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;

public interface CalculateScoreProvider {

    /**
     * 分数计算
     *
     * @param param 参数
     *
     * @return score记分结果
     */
    TopicCalculateScore calculateScore(CalculateParam param);
}
