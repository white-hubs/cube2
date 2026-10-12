package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service;

import com.guhecloud.cube.property.management.client.common.vo.RiskSourceTypeVO;
import com.guhecloud.cube.property.management.client.common.vo.RiskSourceVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceRecordParam;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-15  11:15
 * @Description:
 * @Version: 1.0
 */
public interface VisualizationRiskSourceService {
    RiskSourceVO archiveStatistics();

    List<RiskSourceTypeVO> riskSourceRecord(RiskSourceRecordParam param);
}
