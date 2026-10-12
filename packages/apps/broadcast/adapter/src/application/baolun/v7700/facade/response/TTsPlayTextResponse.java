package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tts文本播放返回参数
 *
 * @author lanchun
 * @date 2024/11/7 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TTsPlayTextResponse {

    /**
     * 正常返回为空, 返回logged表示账号已登录
     */
    private Object data;

    /**
     * 公司名 无需理会
     */
    private String company;

    /**
     * 返回说明 默认空
     */
    private String return_message;

    /**
     * 签名 无需理会
     */
    private String sign;

    /**
     * 返回码 200为正常
     */
    private Integer result;

    /**
     * 操作码 无需理会
     */
    private String actioncode;

    /**
     * 认证凭据
     */
    private String token;
}
