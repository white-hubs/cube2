package com.guhecloud.cube.srv.gen.third.connection.server.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameValueStringIntegerVO {
    private String name;
    private Integer value;
}
