package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserParam {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
}
