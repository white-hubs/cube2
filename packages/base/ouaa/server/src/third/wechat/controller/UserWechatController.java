package com.guhecloud.cube.biz.ouaa.server.third.wechat.controller;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.api.OuaaWechatPublicServiceAccountApi;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.api.config.WechatPublicServiceAccountConfig;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.api.dto.response.WechatPublicServiceSnsAccessTokenResponse;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.param.command.IncreaseUserWechatCommand;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.param.query.UserWechatQueryParam;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.service.UserWechatCommandService;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.service.query.UserWechatQueryService;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.vo.UserSnsWechatAccessTokenVO;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.vo.UserWechatVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserWechatController {

    private final UserWechatCommandService userWechatCommandService;

    private final UserWechatQueryService userWechatQueryService;

    private final DataDictionaryClient dataDictionaryClient;

    private final OuaaWechatPublicServiceAccountApi wechatPublicServiceAccountApi;

    /**
     * 公众号通过code换取网页授权access_token
     *
     * @param code 用户授权码
     *
     * @return 会话信息
     */
    @GetMapping("/user/wechat/sns/oauth2/access-token")
    public UserSnsWechatAccessTokenVO wechatSnsOAuth2AccessToken(
            @RequestParam("code") @NotEmpty(message = "用户授权码不能为空") String code
                                                                ) {
        Optional<DataDictionaryDTO> wePublicServiceConfig = getWePublicServiceConfig();
        if (wePublicServiceConfig.isEmpty() ||
            StringUtils.isBlank(wePublicServiceConfig.get().getVariable())) {
            return null;
        }
        WechatPublicServiceAccountConfig accountConfig = WechatPublicServiceAccountConfig.getAccountConfig(wePublicServiceConfig.get()
                                                                                                                   .getVariable());
        WechatPublicServiceSnsAccessTokenResponse userAccessToken = wechatPublicServiceAccountApi.getPublicServiceSnsAccessTokenByCode(code, accountConfig);
        if (Objects.isNull(userAccessToken)) {
            return null;
        }
        return UserSnsWechatAccessTokenVO.builder()
                .accessToken(userAccessToken.getAccessToken())
                .expiresIn(userAccessToken.getExpiresIn())
                .refreshToken(userAccessToken.getRefreshToken())
                .openId(userAccessToken.getOpenid())
                .scope(userAccessToken.getScope())
                .isSnapshotUser(userAccessToken.getIsSnapshotUser())
                .unionId(userAccessToken.getUnionId())
                .build();
    }

    /**
     * 保存用户微信信息
     *
     * @param increaseUserWechatCommand 新增参数
     */
    @PostMapping("/user/wechat")
    public Long save(@Valid @RequestBody IncreaseUserWechatCommand increaseUserWechatCommand) {
        Optional<DataDictionaryDTO> wePublicServiceConfig = getWePublicServiceConfig();
        if (wePublicServiceConfig.isPresent()) {
            return this.userWechatCommandService.increaseWechatUser(increaseUserWechatCommand);
        }
        return 0L;
    }

    /**
     * 获取用户微信信息列表
     *
     * @param param
     */
    @PostMapping("/user/wechat/list")
    public List<UserWechatVO> getUserWechatList(@Valid @RequestBody UserWechatQueryParam param) {
        return this.userWechatQueryService.getUserWechatList(param);
    }

    private Optional<DataDictionaryDTO> getWePublicServiceConfig() {
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode("THIRD_SERVICE");
        dataDictionaryaramParam.setCode("WX_OFFICIAL_ACCOUNT");
        dataDictionaryaramParam.setDisable(true);
        List<DataDictionaryDTO> dictionaryByItem = dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(dictionaryByItem)) {
            return Optional.empty();
        }
        return Optional.of(dictionaryByItem.get(0));
    }
}
