package com.guhecloud.cube.biz.ouaa.server.controller.wechat;

import com.guhecloud.cube.biz.ouaa.server.config.WechatProperties;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatUserInfoResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.vo.WechatSessionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.WechatSignatureVO;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WechatController {

    private final WechatService wechatService;
    private final WechatProperties wechatProperties;

    /**
     * 根据用户授权码获取会话信息
     *
     * @param code 用户授权码
     *
     * @return 会话信息
     */
    @GetMapping("/oauth2/wechat/jscode2session")
    public WechatSessionVO jscode2session(@RequestParam("code") @NotEmpty(message = "用户授权码不能为空") String code) {
        String token = this.wechatService.jscode2session(code);
        return WechatSessionVO.builder().key(token).expiration(this.wechatProperties.getExpiration()).build();
    }

    /**
     * 获取微信的访问信息
     *
     * @return 会话信息
     */
    @GetMapping("/oauth2/wechat/access/signature")
    public WechatSignatureVO getWechatAccessInfo(@RequestParam(required = false) String url) {
        return wechatService.getWechatAccessInfo(url);
    }

    @GetMapping("/wechat/userinfo")
    public WechatUserInfoResponseDTO getWechatUserInfo() {
        return wechatService.getWechatUserInfo();
    }
}
