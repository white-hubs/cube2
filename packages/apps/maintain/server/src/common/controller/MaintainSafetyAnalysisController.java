package com.guhecloud.cube.srv.gen.maintain.server.common.controller;

import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainSafetyAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.MaintainSafetyAnalysisVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintainSafetyAnalysisController {

    private final MaintainSafetyAnalysisService maintainSafetyAnalysisService;

    /**
     * 安全分析-数据获取
     *
     * @param period 查询时间
     *
     * @return 巡检统计结果
     */
    @GetMapping("/maintain/safety-analysis")
    public MaintainSafetyAnalysisVO maintainSafetyAnalysis(@RequestParam("period") Period period, @RequestParam("type") String type) {
        Assert.isParameterError(period == null, "统计时间范围不允许为空!");
        return maintainSafetyAnalysisService.maintainSafetyAnalysis(period, type);
    }
}
