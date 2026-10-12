package com.guhecloud.cube.property.management.client.risk.source.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskSourceIdTypeVO {
    private Long id;
    private String name;
    private String typeId;
    private Set<Long> tagIds;
}
