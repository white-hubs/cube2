package com.guhecloud.cube.property.management.client.risk.source.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-02-21  15:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceMessageVO {
    private Long riskSourceTotal;
    private List<String> riskSourceName;
}
