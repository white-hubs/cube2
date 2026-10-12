package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatAccessTokenResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatUserInfoResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTelBindEnableEnum;
import com.guhecloud.cube.biz.ouaa.server.param.AuthBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsUnbindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.VerifySmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordTokenAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.sms.SmsTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.wechat.WechatOpenPlatformTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.service.SystemUserAuthService;
import com.guhecloud.cube.biz.ouaa.server.service.UserBindService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.service.sms.SmsService;
import com.guhecloud.cube.biz.ouaa.server.utils.UserWechatUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CLIENT_ID_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CLIENT_SECRET_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CODE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_GRANT_TYPE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_MOBILE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_PASSWORD_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserBindServiceImpl implements UserBindService {
    private final WechatService wechatService;
    private final SmsService smsService;
    private final UserService userService;
    private final UserTelService userTelService;
    private final UserWechatService userWechatService;
    private final SystemUserAuthService systemUserAuthService;
    private final RestTemplate restTemplate;
    @Value(value = "${third-part.auth-server.uri}")
    private String authServerAddr;
    private static final String CLIENT_ID = "client_2";
    private static final String CLIENT_KEY = "cli@pwd#ghy!789";

    @Override
    @Transactional
    public Boolean wechatBind(String code) {
        try {
            //微信认证
            String tokenId = wechatService.auth(code);
            //绑定微信
            WechatAccessTokenResponseDTO accessToken = wechatService.getAccessToken(tokenId);
            bindWechatUser(accessToken, SecurityContextHolder.getPrincipal().getId());
            return true;
        } catch (Exception e) {
            log.error("未知错误", e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("Internal Error");
        }

    }

    @Override
    @Transactional
    public Boolean wechatUnbind() {
        String account = SecurityContextHolder.getPrincipal().getAccount();
        //查询用户微信绑定信息
        User user = userService.getUserByAccount(account);
        Assert.isNotFound(user, "用户不存在！");
        //查询这个微信是否已经绑定了用户
        UserWechat bindInfo = userWechatService.getWechatInfo(user.getId());
        Assert.isNotFound(bindInfo, "该微信未绑定用户，无法解绑！");
        userWechatService.removeById(bindInfo.getId());
        return Boolean.TRUE;

    }

    @Override
    @Transactional
    public Boolean phoneBind(SmsBindParam bindParam) {
        //校验用户是否存在
        Long userId = SecurityContextHolder.getPrincipal().getId();
        User user = userService.getById(userId);
        //校验用户是否绑定
        if (userTelService.checkUserBindExist(userId)) {
            throw new OuaaException(ErrorTypeEnum.USER_HAS_BIND_TEL);
        }
        //校验手机号是否已经被绑定
        if (userTelService.checkUserBindTel(userId, bindParam.tel())) {
            throw new OuaaException(ErrorTypeEnum.OTHER_USER_HAS_BIND_TEL);
        }
        //验证短信验证码
        smsService.verifySmsCode(new VerifySmsCodeParam(bindParam.tel(), bindParam.code(), SmsCodeTypeEnum.TEL_BIND));
        //绑定用户到手机号
        userTelService.relationUserTelIsBind(user.getId(), bindParam.tel());
        //清除用户表里面使用该手机号的用户mobile
        clearUseThisTelData(bindParam.tel());
        //更新用户手机号
        user.setMobile(bindParam.tel());
        userService.updateById(user);
        return Boolean.TRUE;

    }

    @Override
    @Transactional
    public Boolean phoneUnbind(SmsUnbindParam smsUnbindParam) {

        //校验用户是否存在
        Long userId = SecurityContextHolder.getPrincipal().getId();
        User user = userService.getById(userId);
        //校验用户是否已绑定,如果有绑定，说明当前解绑的手机和绑定的手机一致
        Assert.isNotFound(!userTelService.checkUserBindExist(userId), "未查询到绑定的手机号");
        UserTel bindUserTel = userTelService.getBindByUserId(userId);
        //验证短信验证码
        smsService.verifySmsCode(new VerifySmsCodeParam(smsUnbindParam.tel(), smsUnbindParam.code(), SmsCodeTypeEnum.TEL_UNBIND));
        //解绑用户的手机号
        bindUserTel.setIsBind(UserTelBindEnableEnum.UNBIND);
        userTelService.updateById(bindUserTel);
        //更新用户手机号
        user.setMobile(null);
        userService.updateById(user);
        return Boolean.TRUE;
    }

    @Override
    public Object authBind(AuthBindParam param, HttpServletRequest request) {
        //颁发授权登录信息
        String result = accessTokenRequest(param);
        Map<String, Object> resultMap = JsonUtils.deserialize(result, new TypeReference<>() {});
        resultMap = Objects.requireNonNullElseGet(resultMap, HashMap::new);
        bindUser(param.bindType(), param.tokenId(), Long.parseLong(resultMap.get("id").toString()));
        systemUserAuthService.removeAuthInfo(param.tokenId());
        return result;
    }

    private String accessTokenRequest(AuthBindParam authBindParam) {
        var authParam = authBindParam.authParam();
        var params = new LinkedMultiValueMap<>();
        params.add(PARAM_CLIENT_ID_KEY, CLIENT_ID);
        params.add(PARAM_CLIENT_SECRET_KEY, CLIENT_KEY);
        switch (authBindParam.authType()) {
            case PASSWORD -> {
                params.add(PARAM_GRANT_TYPE_KEY, PasswordTokenAuthenticationConverter.GRANT_TYPE.getValue());
                params.add(PARAM_USERNAME_KEY, authParam.get(PARAM_USERNAME_KEY));
                params.add(PARAM_PASSWORD_KEY, authParam.get(PARAM_PASSWORD_KEY));
            }
            case SMS -> {
                params.add(PARAM_GRANT_TYPE_KEY, SmsTokenConverter.GRANT_TYPE.getValue());
                params.add(PARAM_MOBILE_KEY, authParam.get(PARAM_MOBILE_KEY));
                params.add(PARAM_CODE_KEY, authParam.get(PARAM_CODE_KEY));
            }
            case WECHAT -> {
                params.add(PARAM_GRANT_TYPE_KEY, WechatOpenPlatformTokenConverter.GRANT_TYPE.getValue());
                params.add(PARAM_CODE_KEY, authParam.get(PARAM_CODE_KEY));
            }
            default -> throw new UnimplementedException("不支持的授权类型！");

        }
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            var response = restTemplate.postForEntity(authServerAddr, new HttpEntity<>(params, headers), String.class);
            return response.getBody();
        } catch (HttpClientErrorException httpClientErrorException) {
            HttpStatusCode statusCode = httpClientErrorException.getStatusCode();
            if (HttpStatus.CONFLICT.value() == statusCode.value()) {
                throw authBindParam.authType().equals(AuthTypeEnum.WECHAT) ? new OuaaException(ErrorTypeEnum.WECHAT_BIND_USER_NOT_FOUND)
                                                                           : new OuaaException(ErrorTypeEnum.USER_NOT_BOUND_TO_TEL);
            }
            var responseBody = httpClientErrorException.getResponseBodyAsString();
            var ouaaException = JsonUtils.deserialize(responseBody, OuaaException.class);
            throw Objects.requireNonNullElse(ouaaException, new OuaaException(OAuth2ErrorCodes.INVALID_GRANT));
        } catch (Exception e) {
            throw new OuaaException(OAuth2ErrorCodes.INVALID_GRANT);
        }
    }

    private void clearUseThisTelData(String tel) {
        LambdaUpdateChainWrapper<User> updateChainWrapper = new LambdaUpdateChainWrapper<>(userService.getBaseMapper());
        updateChainWrapper.set(User::getMobile, null);
        updateChainWrapper.eq(User::getMobile, tel);
        updateChainWrapper.update();
    }

    private void bindUser(BindTypeEnum bindType, String tokenId, long userId) {
        switch (bindType) {
            case WECHAT:
                WechatAccessTokenResponseDTO accessToken = wechatService.getAccessToken(tokenId);
                bindWechatUser(accessToken, userId);
                break;
            case SMS:
                String mobile = smsService.getAuthInfo(tokenId);
                //获取用户信息,然后用户登录信息
                User user = userService.getById(userId);
                if (userTelService.checkUserBindExist(user.getId())) {
                    throw new OuaaException(ErrorTypeEnum.USER_HAS_BIND_TEL);
                }
                userTelService.relationUserTelIsBind(userId, mobile);
                break;
        }
    }

    private void bindWechatUser(WechatAccessTokenResponseDTO accessTokenResponseDTO, long userId) {
        String unionId = accessTokenResponseDTO.getUnionId();
        String openId = accessTokenResponseDTO.getOpenId();
        //查询这个微信是否已经绑定了用户
        UserWechat bindInfo = userWechatService.getUserWechatByUnionid(unionId);
        Assert.isConflict(Objects.nonNull(bindInfo), "该微信已经绑定了用户，无法再次绑定这个用户！");

        UserWechat userWechat = userWechatService.getWechatInfo(userId);
        WechatUserInfoResponseDTO wechatUserInfo = wechatService.getWechatUserInfo(accessTokenResponseDTO.getAccessToken(), accessTokenResponseDTO.getOpenId());
        if (Objects.isNull(userWechat)) {
            userWechat = UserWechat
                    .builder()
                    .userId(userId)
                    .unionId(unionId)
                    .accessToken(accessTokenResponseDTO.getAccessToken())
                    .refreshToken(accessTokenResponseDTO.getRefreshToken())
                    .avatar(wechatUserInfo.getHeadImgUrl())
                    .nickname(wechatUserInfo.getNickname())
                    .build();
        } else {
            userWechat.setUnionId(unionId);
            userWechat.setAccessToken(accessTokenResponseDTO.getAccessToken());
            userWechat.setRefreshToken(accessTokenResponseDTO.getRefreshToken());
            userWechat.setAvatar(wechatUserInfo.getHeadImgUrl());
            userWechat.setNickname(wechatUserInfo.getNickname());
        }
        UserWechatUtils.setWechatOpenid(userWechat, openId);
        //更新绑定信息
        userWechatService.saveOrUpdate(userWechat);

    }

}
