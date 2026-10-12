package com.guhecloud.cube.base.message2.client.dto;

import lombok.Data;

/**
 * 场景接收用户信息返回参数
 *
 * @author lanchun
 * @date 2023/6/27 11:24
 */
@Data
public class SatMsgSceneReceiveUserDTO {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}
