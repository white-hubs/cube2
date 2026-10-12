package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.service.AnalysisBySynthesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 综合分析
 *
 * @author wql
 * @describe 综合分析
 * @date 2023/9/8 16:15
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnalysisBySynthesisController {

    private final AnalysisBySynthesisService analysisBySynthesisService;

    /**
     * 综合分析卡片
     *
     * @Author wql
     * @Description 综合分析卡片
     * @Date 16:52 2023/9/8
     **/
    @GetMapping("/synthesis/statisticalCard")
    public AnalysisBySynthesisCardVO getAnalysisBySynthesisStatistics() {
        return analysisBySynthesisService.getAnalysisBySynthesisStatistics();
    }

    /**
     * 综合分析市场卡片
     *
     * @Author wql
     * @Description 综合分析市场卡片
     * @Date 16:54 2023/9/8
     **/
    @GetMapping("/synthesis/statisticalMarketCard")
    public AnalysisBySynthesisMarketCardVO getAnalysisBySynthesisMarketStatistics(BaseQuery query) {
        return analysisBySynthesisService.getAnalysisBySynthesisMarketStatistics(query);
    }

}
