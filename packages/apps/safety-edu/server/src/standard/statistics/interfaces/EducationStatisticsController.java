package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.service.EducationStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 安全教育统计
 *
 * @author lanchun
 * @date 2024/1/22 16:54
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EducationStatisticsController {

    private final EducationStatisticsService educationStatisticsService;

    /**
     * 安全教育主题统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education-statistics/themes")
    public List<SafetyEducationThemeRatioVO> getSafetyEducationThemes(@Valid SafetyEducationStatisticsQuery query) {
        return this.educationStatisticsService.getSafetyEducationThemeRatio(query);
    }

    /**
     * 安全教育主题统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education-statistics/permission/themes")
    public List<SafetyEducationThemeRatioVO> getSafetyEducationPermissionThemes(@Valid SafetyEducationStatisticsQuery query) {
        return this.educationStatisticsService.getSafetyEducationPermissionThemeRatio(query);
    }

    /**
     * 安全教育任务数环比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education-statistics/task/chain-ratio")
    public SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(@Valid SafetyEducationStatisticsQuery query) {
        return this.educationStatisticsService.getSafetyEducationTaskChainRatio(query);
    }
}
