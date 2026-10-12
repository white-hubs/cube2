package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.controller;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response.CreateWechatPublicServiceQrCodeResponse;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.service.IdentityPublicServiceAccountService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.vo.CreateWechatPublicServiceQrCodeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WechatQrCodeController {

    private final IdentityPublicServiceAccountService identityPublicServiceAccountService;

    /**
     * 生成公众号二维码
     *
     * @return CreateWechatPublicServiceQrCodeResponse
     */
    @PostMapping("/wechat/public-service-account/qr-code")
    public CreateWechatPublicServiceQrCodeVO createWechatPublicServiceQrCodeResponse() {
        CreateWechatPublicServiceQrCodeResponse wechatPublicServiceQrCodeResponse = this.identityPublicServiceAccountService.createWechatPublicServiceQrCodeResponse();
        if (Objects.isNull(wechatPublicServiceQrCodeResponse)) {
            return null;
        }
        return CreateWechatPublicServiceQrCodeVO.builder()
                .ticket(wechatPublicServiceQrCodeResponse.getTicket())
                .expireSeconds(wechatPublicServiceQrCodeResponse.getExpireSeconds())
                .url(wechatPublicServiceQrCodeResponse.getUrl())
                .build();
    }
}
