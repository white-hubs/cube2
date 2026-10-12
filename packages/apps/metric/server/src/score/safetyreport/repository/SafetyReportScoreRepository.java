package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository;

import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.param.SafetyReportQueryParam;
import com.guhecloud.platform.base.model.Pagination;

public interface SafetyReportScoreRepository {

    Score saveScore(Score score);

    Score getScoreByRequestId(String requestId);

    void removeByRequestId(String requestId);

    Pagination<Score> getPage(SafetyReportQueryParam param);
}
