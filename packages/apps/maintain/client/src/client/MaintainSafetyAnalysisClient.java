package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.vo.MaintainSafetyAnalysisVO;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("maintain")
public interface MaintainSafetyAnalysisClient {

    /**
     * 安全分析-数据获取
     *
     * @param period 查询时间
     *
     * @return 巡检统计结果
     */
    @GetMapping("/maintain/safety-analysis")
    MaintainSafetyAnalysisVO maintainSafetyAnalysis(@RequestParam("period") Period period, @RequestParam("type") String type);
}
