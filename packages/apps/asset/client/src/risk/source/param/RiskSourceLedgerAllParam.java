package com.guhecloud.cube.property.management.client.risk.source.param;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:47
 * @Description:
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskSourceLedgerAllParam {
    private Period period;
}
