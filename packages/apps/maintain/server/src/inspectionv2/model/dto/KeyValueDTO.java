package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueDTO {
    private String key;
    private Long longKey;
    private String value;
    private Long longValue;
}
