package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.UserSource;
import com.guhecloud.cube.biz.ouaa.server.config.WechatProperties;
import com.guhecloud.cube.biz.ouaa.server.dto.WechatResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatAccessTokenResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatUserInfoResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTenantBeloneEnum;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.utils.PasswordUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.UserWechatUtils;
import com.guhecloud.cube.biz.ouaa.server.vo.WechatSignatureVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 微信业务操作实现
 *
 * @author Woody
 * @date 2021/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WechatServiceImpl implements WechatService {

    /**
     * 微信access_token标识前缀
     */
    private static final String USER_WECHAT_ACCESS_TOKEN_KEY = "user:wechat:access_token:";
    /**
     * 微信会话Redis标识前缀
     */
    private static final String WECHAT_SESSION_REDIS_KEY_PREFIX = "wechat:session:";

    /**
     * 微信accessToken缓存
     */
    private static final String WECHAT_TOKEN_REDIS_KEY_PREFIX = "wechat:token:";

    /**
     * 微信jsapiticket缓存
     */
    private static final String WECHAT_TICKET_REDIS_KEY_PREFIX = "wechat:ticket:";

    /**
     * 微信jscode2session接口地址
     */
    private static final String WECHAT_API_JSCODE2SESSION =
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    /**
     * 获取小程序或者公众号access_token地址
     */
    private static final String WECHAT_API_ACCESS_TOKEN =
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String WECHAT_API_LOGIN_ACCESS_TOKEN =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private static final String WECHAT_API_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    /**
     * 获取微信jsapi的ticket
     */
    private static final String WECHAT_API_TICKET =
            "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    private static final String WECHAT_API_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    private static final String SYS_ABILITY_CONFIG = "SYS_ABILITY_CONFIG";
    private static final String WECHAT_ABILITY_CONFIG_CODE = "wechat_login";
    private static final Integer EXPIRATION = 24 * 60 * 60;
    private static final Integer TEMP_WECHAT_AUTH_TOKEN_EXPIRATION = 10;
    private static final String TEMP_WECHAT_AUTH_TOKEN_KEY = "temp:wechat:auth:token:";
    private final UserService userService;
    private final TerminalService terminalService;
    private final UserWechatService userWechatService;
    private final RestTemplate restTemplate;
    private final WechatProperties wechatProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordUtils passwordUtils;
    private final SysDictionaryAdapter sysDictionaryAdapter;

    /**
     * 微信响应结果验证
     *
     * @param response 微信响应信息
     */
    protected void validateResponse(WechatResponseDTO response) {
        Assert.isNotFound(response, "微信服务响应异常");
        int status = ObjectUtils.ifNull(response.getErrcode(), 0);
        if (status == 0) {
            return;
        }
        log.error("Wechat authenticate failed: [{}] {}", status, response.getErrmsg());
        if (status == -1) {
            throw new AccessDeniedException("微信系统繁忙，请稍候再试");
        } else if (status == 40029 || status == 40163) {
            throw new SessionAuthenticationException("临时令牌无效");
        } else if (status == 45011) {
            throw new AccessDeniedException("操作过于频率，请稍候再试");
        } else if (status == 40226) {
            throw new AccessDeniedException("拒绝登录");
        }
        throw new SessionAuthenticationException(response.getErrmsg());
    }

    /**
     * 执行请求操作
     *
     * @param supplier 接口请求回调犯法
     * @param <T>      微信响应数据类型
     *
     * @return 微信响应结果
     */
    protected <T extends WechatResponseDTO> T execute(@NonNull Supplier<T> supplier) {
        T response = supplier.get();
        this.validateResponse(response);
        return response;
    }

    @Override
    public String jscode2session(@NonNull String code) {
        // 获取密钥
        String terminalCode = SessionContextHolder.getRequester().getTerminal();

        Terminal terminal = this.terminalService.getByCode(terminalCode);
        Assert.isNotFound(terminal, "不存在访问端信息!");
        String id = Objects.requireNonNull(terminal.getAppId(), "App id not specified");
        String secret = Objects.requireNonNull(terminal.getAppSecret(), "App secret not specified");
        int expiration = Objects.requireNonNull(EXPIRATION, "Wechat session expiration not specified");
        Assert.isOutOfRange(expiration <= 0, "Wechat session expiration must be greater than 0");
        WechatResponseDTO.Session session = this.execute(() -> {
            String url = String.format(WECHAT_API_JSCODE2SESSION, id, secret, code);
            String sessionStr = this.restTemplate.getForObject(url, String.class);
            return JsonUtils.deserialize(sessionStr, WechatResponseDTO.Session.class);
        });
        String token = UUID.randomUUID().toString();
        String key = WECHAT_SESSION_REDIS_KEY_PREFIX + token;
        this.redisTemplate.opsForValue().set(key, session, expiration, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public WechatResponseDTO.Session token2session(@NonNull String token) {
        String key = WECHAT_SESSION_REDIS_KEY_PREFIX + token;
        WechatResponseDTO.Session session = (WechatResponseDTO.Session) this.redisTemplate
                .opsForValue().get(key);
        this.redisTemplate.delete(key);
        return session;
    }

    @Override
    public boolean isRegistered(@NonNull String mobile) {
        return this.userService.lookupUser(mobile) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(@NonNull WechatResponseDTO.Session session, @NonNull User user) {
        //查询账户是否存在
        User userByAccount = userService.getUserByAccount(user.getUsername());
        User userByMobile = userService.getUserByMobile(user.getMobile());
        // 关联微信用户/租户/角色信息
        UserWechat userWechat = UserWechat.builder().id(IdWorker.getId()).userId(user.getId()).unionId(session.getUnionId()).build();
        UserWechatUtils.setWechatOpenid(userWechat, session.getOpenId());
        if (Objects.isNull(userByAccount) && Objects.isNull(userByMobile)) {
            userService.save(user);
            // 无tenant则不加关联关系
            UserTenant userTenant = null;
            if (!StringUtils.isBlank(SessionContextHolder.getRequester().getTenant().toString())) {
                userTenant = UserTenant.builder().id(IdWorker.getId()).userId(user.getId())
                        .belongTenant(UserTenantBeloneEnum.NOT_BELONG.name())
                        .tenant(SessionContextHolder.getRequester().getTenant()).build();
            }
            List<UserRole> userRoles = Collections.emptyList();
            this.userWechatService.initializeWechatUserConfiguration(userWechat, userTenant, userRoles);
            return true;
        } else {
            long userId = Objects.nonNull(userByAccount) ? userByAccount.getId() : userByMobile.getId();
            userWechat.setUserId(userId);
            //保存微信用户信息
            this.userWechatService.save(userWechat);
        }

        return false;
    }

    @Override
    public WechatSignatureVO getWechatAccessInfo(String url) {
        url = StringUtils.isBlank(url) ? "http://ghyadmin-0gedfvh04320e725-1307894734.tcloudbaseapp.com" : url;
        String id = Objects.requireNonNull(this.wechatProperties.getId(), "App id not specified");
        String secret = Objects.requireNonNull(this.wechatProperties.getSecret(), "App secret not specified");
        //获取token
        String tokenKey = WECHAT_TOKEN_REDIS_KEY_PREFIX + id;
        Object acToken = this.redisTemplate.opsForValue().get(tokenKey);
        if (acToken == null) {
            WechatResponseDTO.AccessToken accessToken = this.execute(() -> {
                String tokenUrl = String.format(WECHAT_API_ACCESS_TOKEN, id, secret);
                return this.restTemplate.getForObject(tokenUrl, WechatResponseDTO.AccessToken.class);
            });
            this.redisTemplate.opsForValue()
                    .set(tokenKey, accessToken.getAccessToken(), accessToken.getExpiresIn() - 1800, TimeUnit.SECONDS);
            acToken = accessToken.getAccessToken();
        }

        //获取ticket
        String ticketKey = WECHAT_TICKET_REDIS_KEY_PREFIX + id;
        Object ticket = this.redisTemplate.opsForValue().get(ticketKey);
        if (ticket == null) {
            String accessToken1 = acToken.toString();
            WechatResponseDTO.JsTicket jsTicket = this.execute(() -> {
                String ticketUrl = String.format(WECHAT_API_TICKET, accessToken1);
                return this.restTemplate.getForObject(ticketUrl, WechatResponseDTO.JsTicket.class);
            });
            this.redisTemplate.opsForValue().set(ticketKey, jsTicket.getTicket(), jsTicket.getExpiresIn() - 1800, TimeUnit.SECONDS);
            ticket = jsTicket.getTicket();
        }
        //生成签名
        WechatSignatureVO signatureVO = new WechatSignatureVO();
        //参数排序
        String currentTimeStr = String.valueOf(System.currentTimeMillis());
        Long wecharTime = Long.valueOf(currentTimeStr.substring(0, currentTimeStr.length() - 3));
        String param = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s",
                                     ticket.toString(), signatureVO.getNonceStr(), wecharTime, url);
        String sha1 = getSha1(param);
        signatureVO.setAppId(id);
        signatureVO.setSignature(sha1);
        signatureVO.setTimestamp(wecharTime);
        return signatureVO;
    }

    private Terminal checkTerminalWechatConfigInfo() {
        // 获取密钥
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        Assert.isNotFound(terminal, "不存在访问端信息!");
        Objects.requireNonNull(terminal.getAppId(), "App id not specified");
        Objects.requireNonNull(terminal.getAppSecret(), "App secret not specified");
        int expiration = Objects.requireNonNull(EXPIRATION, "Wechat session expiration not specified");
        Assert.isOutOfRange(expiration <= 0, "Wechat session expiration must be greater than 0");
        return terminal;
    }

    private WechatAccessTokenResponseDTO getWechatAccessToken(String code) {
        Terminal terminal = checkTerminalWechatConfigInfo();
        String appId = terminal.getAppId();
        String appSecret = terminal.getAppSecret();
        String accessTokenUrl = String.format(WECHAT_API_LOGIN_ACCESS_TOKEN, appId, appSecret, code);
        ResponseEntity<String> accessTokenResponseEntity = restTemplate.getForEntity(accessTokenUrl, String.class);
        WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO = JsonUtils.deserialize(accessTokenResponseEntity.getBody(), WechatAccessTokenResponseDTO.class);
        Assert.isNotFound(wechatAccessTokenResponseDTO, "获取微信access_token失败！");
        Integer errcode = wechatAccessTokenResponseDTO.getErrcode();
        if (errcode != null && errcode != 0) {
            log.error("获取微信access_token失败！,code:{},msg:{}", errcode, wechatAccessTokenResponseDTO.getErrmsg());
            throw new ServiceException(String.format("获取微信access_token失败！,code:%s,msg:%s", errcode, wechatAccessTokenResponseDTO.getErrmsg()));
        }
        return wechatAccessTokenResponseDTO;

    }

    @Override
    public WechatAccessTokenResponseDTO getAccessToken(String tokenId) {
        Assert.isParameterError(StringUtils.isBlank(tokenId), "临时授权tokenId为空！");
        String cacheStr = stringRedisTemplate.opsForValue().get(TEMP_WECHAT_AUTH_TOKEN_KEY + tokenId);
        if (StringUtils.isBlank(cacheStr)) {
            throw new ServiceException("请重新进行微信授权！");
        }

        return JsonUtils.deserialize(cacheStr, WechatAccessTokenResponseDTO.class);
    }

    @Transactional
    @Override
    public User register2(WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO, WechatUserInfoResponseDTO wechatUserInfo) {
        User user = createWechatNewUser(wechatUserInfo);
        // 注册/更新用户
        if (this.userService.registerOrUpdate(user)) {
            // 关联微信用户/租户/角色信息
            UserWechat userWechat = UserWechat
                    .builder()
                    .id(IdWorker.getId())
                    .userId(user.getId())
                    .unionId(wechatAccessTokenResponseDTO.getUnionId())
                    .avatar(wechatUserInfo.getHeadImgUrl())
                    .nickname(wechatUserInfo.getNickname())
                    .build();
            UserWechatUtils.setWechatOpenid(userWechat, wechatAccessTokenResponseDTO.getOpenId());
            // 无tenant则不加关联关系
            UserTenant userTenant = null;
            if (!StringUtils.isBlank(SessionContextHolder.getRequester().getTenant().toString())) {
                userTenant = UserTenant.builder().id(IdWorker.getId()).userId(user.getId())
                        .belongTenant(UserTenantBeloneEnum.NOT_BELONG.name())
                        .tenant(SessionContextHolder.getRequester().getTenant()).build();
            }
            List<UserRole> userRoles = Collections.emptyList();
            this.userWechatService.initializeWechatUserConfiguration(userWechat, userTenant, userRoles);
        }
        return user;
    }

    @Override
    public WechatUserInfoResponseDTO getWechatUserInfo(String accessToken, String openId) {
        Assert.isParameterError(StringUtils.isBlank(accessToken), "access_token为空！");
        Assert.isParameterError(StringUtils.isBlank(openId), "openId为空！");

        ResponseEntity<byte[]> userinfoEntity = restTemplate.getForEntity(String.format(WECHAT_API_USER_INFO, accessToken, openId), byte[].class);
        HttpHeaders headers = userinfoEntity.getHeaders();
        log.info("userinfo headers:{}", headers);
        log.info("default charset:{}", Charset.defaultCharset());
        byte[] bodyBytes = userinfoEntity.getBody();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        log.info("编码后微信用户信息:{}", body);

        WechatUserInfoResponseDTO wechatUserInfoResponseDTO = JsonUtils.deserialize(body, WechatUserInfoResponseDTO.class);
        wechatUserInfoResponseDTO = Objects.requireNonNullElse(wechatUserInfoResponseDTO, new WechatUserInfoResponseDTO());
        Integer errCode = wechatUserInfoResponseDTO.getErrcode();
        if (Objects.nonNull(errCode) && errCode != 0) {
            throw new ServiceException(String.format("获取微信用户信息失败！,code:%s,msg:%s", errCode, wechatUserInfoResponseDTO.getErrmsg()));
        }
        return wechatUserInfoResponseDTO;
    }

    @Override
    public WechatUserInfoResponseDTO getWechatUserInfo() {
        try {
            Long userId = SecurityContextHolder.getPrincipal().getId();
            //获取用户access_token
            Object accessTokenObj = redisTemplate.opsForValue().get(USER_WECHAT_ACCESS_TOKEN_KEY + userId);
            String accessToken;
            UserWechat wechatInfo = userWechatService.getWechatInfo(userId);
            if (Objects.isNull(accessTokenObj)) {
                Terminal terminal = checkTerminalWechatConfigInfo();
                try {
                    //根据refresh_token获取最新的access_token
                    ResponseEntity<String> wechatAccessTokenResponseDTOResponseEntity = restTemplate.getForEntity(String.format(WECHAT_API_REFRESH_TOKEN, terminal.getAppId(), wechatInfo.getRefreshToken()), String.class);
                    WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO = JsonUtils.deserialize(wechatAccessTokenResponseDTOResponseEntity.getBody(), WechatAccessTokenResponseDTO.class);
                    wechatAccessTokenResponseDTO = Objects.requireNonNullElseGet(wechatAccessTokenResponseDTO, WechatAccessTokenResponseDTO::new);
                    Assert.isNotFound(wechatAccessTokenResponseDTO, "从缓存获取微信access_token失败！");
                    wechatInfo.setRefreshToken(wechatAccessTokenResponseDTO.getRefreshToken());
                    wechatInfo.setAccessToken(wechatAccessTokenResponseDTO.getAccessToken());
                    userWechatService.updateById(wechatInfo);
                    accessToken = wechatAccessTokenResponseDTO.getAccessToken();
                    //缓存access_token
                    redisTemplate.opsForValue().set(USER_WECHAT_ACCESS_TOKEN_KEY + userId, accessToken, 1, TimeUnit.HOURS);
                } catch (Exception e) {
                    log.error("refresh_token获取微信access_token失败：{}", e.getMessage());
                    throw e;
                }

            } else {
                accessToken = (String) accessTokenObj;
            }
            //获取用户信息
            return getWechatUserInfo(accessToken, UserWechatUtils.getWechatOpenid(wechatInfo));
        } catch (Exception e) {
            log.error("获取微信用户信息失败：{}", e.getMessage());
            throw new ServiceException("微信用户信息获取失败");
        }

    }

    @Transactional
    @Override
    public void updateWechatToken(Long userId, WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO) {
        String accessToken = wechatAccessTokenResponseDTO.getAccessToken();
        String refreshToken = wechatAccessTokenResponseDTO.getRefreshToken();
        String openId = wechatAccessTokenResponseDTO.getOpenId();
        Assert.isParameterError(Objects.isNull(userId), "用户ID不能为空");
        Assert.isParameterError(Objects.isNull(openId), "openId不能为空");
        Assert.isParameterError(Objects.isNull(wechatAccessTokenResponseDTO.getExpiresIn()), "access_token过期时间不能为空");
        try {
            WechatUserInfoResponseDTO wechatUserInfo = getWechatUserInfo(accessToken, openId);
            UserWechat wechatInfo = userWechatService.getWechatInfo(userId);
            Assert.isNotFound(wechatUserInfo, "微信用户信息获取失败！");
            wechatInfo.setAccessToken(accessToken);
            wechatInfo.setRefreshToken(refreshToken);
            wechatInfo.setNickname(wechatUserInfo.getNickname());
            wechatInfo.setAvatar(wechatUserInfo.getHeadImgUrl());
            //获取终端信息
            UserWechatUtils.setWechatOpenid(wechatInfo, openId);

            userWechatService.updateById(wechatInfo);
        } catch (Exception e) {
            log.error("用户微信信息更新失败", e);
        }

    }

    @Override
    public void cacheUserWechatAccessToken(Long userId, String accessToken) {
        Assert.isParameterError(Objects.isNull(userId), "用户ID不能为空");
        Assert.isParameterError(StringUtils.isBlank(accessToken), "accessToken不能为空");
        redisTemplate.opsForValue().set(USER_WECHAT_ACCESS_TOKEN_KEY + userId, accessToken, 1, TimeUnit.HOURS);
    }

    @Override
    public UserWechat getUserWechatByUnionid(String unionId) {
        return userWechatService.getUserWechatByUnionid(unionId);
    }

    @Override
    public boolean checkWechatAbility() {
        SysDictionaryDTO sysDictionaryDTO = sysDictionaryAdapter.getSysDictionaryByCodeAndItemCode(SYS_ABILITY_CONFIG, WECHAT_ABILITY_CONFIG_CODE);
        if (Objects.isNull(sysDictionaryDTO)) {
            return false;
        }
        String variable = sysDictionaryDTO.getVariable();
        if (StringUtils.isBlank(variable)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(variable);
        } catch (Exception e) {
            log.error("解析字典变量失败", e);
            return false;
        }

    }

    @Transactional
    @Override
    public void bindUserWechat(Long userId, WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO) {
        String unionId = wechatAccessTokenResponseDTO.getUnionId();
        UserWechat wechatInfo = userWechatService.getWechatInfo(userId);
        String accessToken = wechatAccessTokenResponseDTO.getAccessToken();
        String openId = wechatAccessTokenResponseDTO.getOpenId();
        WechatUserInfoResponseDTO wechatUserInfo = getWechatUserInfo(accessToken, openId);
        if (Objects.isNull(wechatInfo)) {
            wechatInfo = UserWechat.builder()
                    .userId(userId)
                    .unionId(unionId)
                    .accessToken(wechatAccessTokenResponseDTO.getAccessToken())
                    .refreshToken(wechatAccessTokenResponseDTO.getRefreshToken())
                    .nickname(wechatUserInfo.getNickname())
                    .avatar(wechatUserInfo.getHeadImgUrl())
                    .build();
            UserWechatUtils.setWechatOpenid(wechatInfo, wechatAccessTokenResponseDTO.getOpenId());
            userWechatService.saveOrUpdate(wechatInfo);
        } else {
            wechatInfo.setUnionId(unionId);
            wechatInfo.setAccessToken(wechatAccessTokenResponseDTO.getAccessToken());
            wechatInfo.setRefreshToken(wechatAccessTokenResponseDTO.getRefreshToken());
            wechatInfo.setNickname(wechatUserInfo.getNickname());
            wechatInfo.setAvatar(wechatUserInfo.getHeadImgUrl());
            UserWechatUtils.setWechatOpenid(wechatInfo, wechatAccessTokenResponseDTO.getOpenId());
            userWechatService.updateById(wechatInfo);
        }

    }

    @Override
    public UserWechat getUserWechatByUnionidOrOpenId(String unionId, String openId) {
        if (StringUtils.isBlank(unionId) && StringUtils.isBlank(openId)) {
            log.error("unionId and openId is empty");
            return null;
        }
        UserWechat userWechat = null;
        if (!StringUtils.isBlank(unionId)) {
            userWechat = getUserWechatByUnionid(unionId);
        }
        if (Objects.isNull(userWechat) && !StringUtils.isBlank(openId)) {
            userWechat = getUserWechatByOpenid(openId);
        }
        return userWechat;

    }

    @Override
    public String auth(String code) {
        if (!checkWechatAbility()) {
            throw new ServiceException("微信能力未开启！");
        }
        WechatAccessTokenResponseDTO wechatAccessToken = getWechatAccessToken(code);
        String tokenId = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(TEMP_WECHAT_AUTH_TOKEN_KEY +
                                              tokenId, Objects.requireNonNullElse(JsonUtils.serialize(wechatAccessToken), ""), TEMP_WECHAT_AUTH_TOKEN_EXPIRATION, TimeUnit.MINUTES);
        return tokenId;
    }

    @Override
    public void removeAuthInfo(String tokenId) {
        stringRedisTemplate.delete(TEMP_WECHAT_AUTH_TOKEN_KEY + tokenId);
    }

    private UserWechat getUserWechatByOpenid(String openId) {
        if (StringUtils.isBlank(openId)) {
            log.error("openId is empty");
            return null;
        }
        return userWechatService.getUserWechatByOpenid(openId);
    }

    private User createWechatNewUser(WechatUserInfoResponseDTO wechatUserInfo) {
        // 注册新用户并发送用户注册事务消息
        Long id = IdWorker.getId();
        //初始化密码
        String password = passwordUtils.getInitializePassword();
        String wechatUsername = getWechatUsername();
        Gender gender = Gender.NONE;
        Integer sex = wechatUserInfo.getSex();
        if (Objects.nonNull(sex)) {
            if (sex.equals(1)) {
                gender = Gender.MALE;
            } else if (sex.equals(2)) {
                gender = Gender.FEMALE;
            }
        }
        String headImgUrl = wechatUserInfo.getHeadImgUrl();

        return User.builder()
                .id(id)
                .name(wechatUserInfo.getNickname())
                .avatar(headImgUrl)
                .gender(gender)
                .username(wechatUsername)
                .admin(false)
                .password(password)
                .source(UserSource.WECHAT_V2.name())
                .enabled(true)
                .createBy("SYSTEM")
                .updateBy("SYSTEM")
                .build();
    }

    private String getWechatUsername() {
        StringBuilder usernameBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            usernameBuilder
                    .append("wx_")
                    .append(id);
            User user = userService.getUserByAccount(usernameBuilder.toString());
            if (Objects.isNull(user)) {
                return usernameBuilder.toString();
            }
        }
        throw new ServiceException("创建微信用户名失败！");

    }

    /**
     * 对字符串进行SHA1加密
     *
     * @param str
     *
     * @return
     */
    private String getSha1(String str) {

        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'
        };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
