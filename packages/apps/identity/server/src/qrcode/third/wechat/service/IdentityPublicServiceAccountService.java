package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.service;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.api.IdentityPublicServiceAccountApi;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response.CreateWechatPublicServiceQrCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IdentityPublicServiceAccountService {

    private final IdentityPublicServiceAccountApi identityPublicServiceAccountApi;

    public CreateWechatPublicServiceQrCodeResponse createWechatPublicServiceQrCodeResponse() {
        return this.identityPublicServiceAccountApi.createWechatPublicServiceQrCode();
    }
}
