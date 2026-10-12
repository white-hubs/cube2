package com.guhecloud.cube.biz.metric.server.score.safetyreport.controller;

import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.service.CalculateScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 指标分数计算接口管理
 *
 * @author lanchun
 * @date 2025/6/4 16:09
 */
@Slf4j
@RestController
public class CalculateScoreController {

    private final CalculateScoreService calculateScoreService;

    public CalculateScoreController(CalculateScoreService calculateScoreService) {
        this.calculateScoreService = calculateScoreService;
    }

    /**
     * 获取规则分数
     *
     * @param param
     *
     * @return 指标结果
     */
    @PostMapping("/calculate/rule/score")
    public TopicCalculateScore calculateRuleScore(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.calculateRuleScore(param);
    }

    /**
     * 扩展获取规则结果建议
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/calculate/rule/score/suggestion")
    public TopicCalculateScore getRuleResultSuggestion(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.getRuleResultSuggestion(param);
    }

    /**
     * 扩展获取规则内容
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/calculate/rule/content")
    public TopicCalculateScore getRuleContent(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.getRuleContent(param);
    }

    /**
     * 获取父级项分数
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/calculate/parent/score")
    public TopicCalculateScore calculateParentItemScore(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.calculateParentItemScore(param);
    }

    /**
     * 计算总分
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/calculate/score")
    public Score calculateScore(@Valid @RequestBody CalculateParam param) {
        return calculateScoreService.calculateTotalScore(param);
    }
}
