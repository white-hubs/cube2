package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisMarketCardVO;

/**
 * @author wql
 * @describe 综合分析
 * @date 2023/9/8 16:41
 */
public interface AnalysisBySynthesisService {
    AnalysisBySynthesisCardVO getAnalysisBySynthesisStatistics();

    AnalysisBySynthesisMarketCardVO getAnalysisBySynthesisMarketStatistics(BaseQuery query);
}
