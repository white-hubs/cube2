package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service;

import com.guhecloud.cube.property.management.client.risk.source.param.PartsDetailsParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceParts;

import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  18:52
 * @Description:
 * @Version: 1.0
 */
public interface RiskSourcePartsService {
    Boolean insertBatch(List<PartsDetailsParam> params, Long riskSourceLedgerId);

    Boolean insertBatch(List<RiskSourceParts> params);

    Boolean deleteBatch(Set<Long> riskSourceLedgerId);
}
