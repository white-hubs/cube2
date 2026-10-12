package com.guhecloud.cube.biz.metric.server.score.safetyreport.controller;

import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;
import com.guhecloud.cube.biz.metric.server.score.param.SafetyReportQueryParam;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.SafetyReportScoreRepository;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.service.CalculateScoreService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 安全分析报告接口管理
 *
 * @author lanchun
 * @date 2025/6/4 16:09
 */
@Slf4j
@RestController
public class SafetyReportController {

    private final SafetyReportScoreRepository safetyReportScoreRepository;

    private final CalculateScoreService calculateScoreService;

    public SafetyReportController(SafetyReportScoreRepository safetyReportScoreRepository, CalculateScoreService calculateScoreService) {
        this.safetyReportScoreRepository = safetyReportScoreRepository;
        this.calculateScoreService = calculateScoreService;
    }

    /**
     * 获取安全分析报告分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/safety/reports")
    public Pagination<Score> getSafetyReportPage(SafetyReportQueryParam param) {
        return safetyReportScoreRepository.getPage(param);
    }

    /**
     * 获取安全分析报告详情
     *
     * @param requestId
     *
     * @return
     */
    @GetMapping("/safety/reports/{requestId}")
    public Score getScoreByRequestId(@PathVariable String requestId) {
        return safetyReportScoreRepository.getScoreByRequestId(requestId);
    }

    /**
     * 获取报告建议
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/safety/reports/suggestion")
    public Score getSafetyResultSuggestion(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.getSafetyResultSuggestion(param);
    }
}
