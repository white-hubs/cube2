package com.guhecloud.cube.srv.gen.adapter.access.control.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDoorStatusVo {
    private String doorIndexCode;
    private Integer doorState;
}
