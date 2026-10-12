package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.constants;

public class PublicServiceAccountConstant {

    /**
     * 获取access_token接口地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    /**
     * 创建二维码接口地址
     */
    public static final String CREATE_QR_CODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    /**
     * 微信公众服务号授权信息缓存的key
     */
    public static final String ACCESS_TOKEN_CACHE_KEY = "wechatPublicServiceAccountAccessTokenCache:%s";

    /**
     * 配置缓存key
     */
    public static final String CONFIG_CACHE_KEY = "DICTIONARY:THIRD_SERVICE:%s:WX_OFFICIAL_ACCOUNT";

    private PublicServiceAccountConstant() {}
}
