package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatSignatureVO {

    /**
     * 时间戳
     */
    private Long timestamp = 0l;

    /**
     * 随机字符串
     */
    private String nonceStr = UUID.randomUUID().toString().replace("-", "");

    /**
     * appId
     */
    private String appId;

    /**
     * 签名
     */
    private String signature;
}
