package com.guhecloud.cube.property.management.client.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueLongStringVO {
    private Long key;
    private String value;
}
