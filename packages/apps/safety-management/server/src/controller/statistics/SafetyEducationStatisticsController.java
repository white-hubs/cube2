package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.ExamTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationMarketTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyEducationStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 安全教育统计
 *
 * @author lanchun
 * @date 2023/9/8 11:51
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyEducationStatisticsController {

    private final SafetyEducationStatisticsService safetyEducationStatisticsService;

    /**
     * 安全教育主题统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/themes")
    public List<SafetyEducationThemeRatioVO> getSafetyEducationThemes(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getSafetyEducationThemeRatio(query);
    }

    /**
     * 安全教育任务数量统计
     *
     * @param query SafetyEducationStatisticsQuery
     *
     * @return SafetyEducationTaskStatisticsVO
     */
    @GetMapping("/safety-education-statistics/task-count")
    public SafetyEducationTaskStatisticsVO getSafetyEducationTaskStatistics(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getSafetyEducationTaskStatistics(query);
    }

    /**
     * 市场培训任务统计列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/market/cultivate")
    public Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketCultivateStatistics(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getMarketCultivateStatistics(query);
    }

    /**
     * 市场考试任务统计列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/market/exam")
    public Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketExamStatistics(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getMarketExamStatistics(query);
    }

    /**
     * 考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/exam/page")
    public Pagination<ExamTaskPageVO> getExamTaskPage(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getExamTaskPage(query);
    }

    /**
     * 培训任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/cultivate/page")
    public Pagination<CultivateTaskPageVO> getCultivateTaskPage(@Valid SafetyEducationStatisticsQuery query) {
        return this.safetyEducationStatisticsService.getCultivateTaskPage(query);
    }

    /**
     * 安全教育完成数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/count/completed")
    public Long safetyEducationCountCompleted(BaseStatisticsQuery query) {
        return safetyEducationStatisticsService.countCompleted(query);
    }

    /**
     * 安全教育任务频率
     *
     * @param safetyEducationStatisticsQuery
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/count/task-ratio")
    public SafetyEducationTaskRatioVO getSafetyEducationTaskRatio(SafetyEducationStatisticsQuery safetyEducationStatisticsQuery) {
        return safetyEducationStatisticsService.getSafetyEducationTaskRatio(safetyEducationStatisticsQuery);
    }
}
