package com.guhecloud.cube.srv.gen.energymanagement.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueDTO {
    private Long key;
    private String code;
    private String value;
}
