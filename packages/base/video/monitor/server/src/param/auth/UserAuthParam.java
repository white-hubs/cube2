package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户
 *
 * @author lanchun
 * @date 2023/3/22 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAuthParam {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}
