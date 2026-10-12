package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.service;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query.SafetyEducationStatisticsQuery;

import java.util.List;

public interface EducationStatisticsService {

    /**
     * 安全教育主题占比统计
     *
     * @param query
     *
     * @return
     */
    List<SafetyEducationThemeRatioVO> getSafetyEducationThemeRatio(SafetyEducationStatisticsQuery query);

    /**
     * 安全教育主题占比统计
     *
     * @param query
     *
     * @return
     */
    List<SafetyEducationThemeRatioVO> getSafetyEducationPermissionThemeRatio(SafetyEducationStatisticsQuery query);

    /**
     * 安全教育任务环比统计
     *
     * @param query
     *
     * @return
     */
    SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(SafetyEducationStatisticsQuery query);

}
