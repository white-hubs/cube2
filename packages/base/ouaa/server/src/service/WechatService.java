package com.guhecloud.cube.biz.ouaa.server.service;

import com.guhecloud.cube.biz.ouaa.server.dto.WechatResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatAccessTokenResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatUserInfoResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.vo.WechatSignatureVO;

/**
 * 微信业务操作接口
 *
 * @author Woody
 * @date 2021/7/25
 */
public interface WechatService {
    /**
     * 根据临时令牌获取会话信息
     *
     * @param code 临时令牌
     *
     * @return 会话标识
     */
    String jscode2session(String code);

    /**
     * 根据临时令牌获取会话信息
     *
     * @param token 临时令牌
     *
     * @return 会话信息
     */
    WechatResponseDTO.Session token2session(String token);

    /**
     * 判断手机号是否已注册
     *
     * @param mobile 手机号
     *
     * @return true/false
     */
    boolean isRegistered(String mobile);

    /**
     * 注册用户并返回是否注册成功
     *
     * @param session 会话信息
     * @param user    用户信息
     *
     * @return true/false
     */
    boolean register(WechatResponseDTO.Session session, User user);

    /**
     * 获取微信的访问签名
     *
     * @return
     */
    WechatSignatureVO getWechatAccessInfo(String url);

    WechatAccessTokenResponseDTO getAccessToken(String tokenId);


    User register2(WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO, WechatUserInfoResponseDTO wechatUserInfo);


    WechatUserInfoResponseDTO getWechatUserInfo(String accessToken, String openId);

    WechatUserInfoResponseDTO getWechatUserInfo();

    void updateWechatToken(Long userId, WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO);

    void cacheUserWechatAccessToken(Long userId, String accessToken);

    UserWechat getUserWechatByUnionid(String unionId);

    boolean checkWechatAbility();

    void bindUserWechat(Long userId, WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO);
    UserWechat getUserWechatByUnionidOrOpenId(String unionId, String openId);

    String auth(String code);

    void removeAuthInfo(String tokenId);
}
