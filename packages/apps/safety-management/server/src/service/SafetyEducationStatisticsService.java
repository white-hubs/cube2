package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.ExamTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationMarketTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationTaskStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface SafetyEducationStatisticsService {

    /**
     * 安全教育主题占比统计
     *
     * @param query
     *
     * @return
     */
    List<SafetyEducationThemeRatioVO> getSafetyEducationThemeRatio(SafetyEducationStatisticsQuery query);

    /**
     * 安全教育任务环比统计
     *
     * @param query
     *
     * @return
     */
    SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(SafetyEducationStatisticsQuery query);

    /**
     * 安全教育任务占比统计
     *
     * @param query
     *
     * @return
     */
    SafetyEducationTaskRatioVO getSafetyEducationTaskRatio(SafetyEducationStatisticsQuery query);

    /**
     * 安全教育频率统计
     *
     * @param query
     *
     * @return
     */
    AppSafetyWorkSituationVO.WorkSituationVO getSafetyEducationSituation(BaseQuery query);

    /**
     * 安全教育已完成的任务数
     *
     * @param query
     *
     * @return
     */
    Long countCompleted(BaseStatisticsQuery query);

    /**
     * 安全教育任务统计
     *
     * @param query
     *
     * @return
     */
    SafetyEducationTaskStatisticsVO getSafetyEducationTaskStatistics(SafetyEducationStatisticsQuery query);

    /**
     * 市场培训任务统计列表
     *
     * @param query
     *
     * @return
     */
    Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketCultivateStatistics(SafetyEducationStatisticsQuery query);

    /**
     * 市场考试任务统计列表
     *
     * @param query
     *
     * @return
     */
    Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketExamStatistics(SafetyEducationStatisticsQuery query);

    /**
     * 考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    Pagination<ExamTaskPageVO> getExamTaskPage(SafetyEducationStatisticsQuery query);

    /**
     * 培训任务分页列表
     *
     * @param query
     *
     * @return
     */
    Pagination<CultivateTaskPageVO> getCultivateTaskPage(SafetyEducationStatisticsQuery query);
}
