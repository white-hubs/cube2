package com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.constants;

public class PublicServiceAccountConstant {

    /**
     * 获取access_token接口地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    /**
     * 发送模板消息接口地址
     */
    public static final String TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    /**
     * 微信公众服务号授权信息缓存的key
     */
    public static final String ACCESS_TOKEN_CACHE_KEY = "wechatPublicServiceAccountAccessTokenCache:%s";

    private PublicServiceAccountConstant() {}
}
