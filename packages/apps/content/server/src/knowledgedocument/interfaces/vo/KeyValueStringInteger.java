package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueStringInteger {
    private String key;
    private Integer value;
}
