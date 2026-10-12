package com.guhecloud.cube.srv.gen.safety.education.client;

import com.guhecloud.cube.srv.gen.safety.education.client.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.client.vo.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.education.client.vo.SafetyEducationThemeRatioVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import java.util.List;

@FeignClient(name = "safetyEducation")
public interface CultivateTaskClient {

    /**
     * 培训主题统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education-statistics/themes")
    List<SafetyEducationThemeRatioVO> getSafetyEducationThemes(@SpringQueryMap @Valid SafetyEducationStatisticsQuery query);

    /**
     * 安全教育任务数环比同比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education-statistics/task/chain-ratio")
    SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(@SpringQueryMap @Valid SafetyEducationStatisticsQuery query);

}
