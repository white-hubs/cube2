package com.guhecloud.cube.biz.ouaa.server.third.wechat.api.constants;

public class WechatApiUrlConstant {

    /**
     * 通过code换取网页授权access_token地址
     */
    public static final String WECHAT_PUBLIC_SERVICE_SNS_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private WechatApiUrlConstant() {

    }
}
