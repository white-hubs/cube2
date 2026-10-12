package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueResult {
    private String key;
    private String value;
}
