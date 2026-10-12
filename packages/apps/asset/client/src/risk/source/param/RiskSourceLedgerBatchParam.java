package com.guhecloud.cube.property.management.client.risk.source.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskSourceLedgerBatchParam {
    private List<Long> idList;
}
