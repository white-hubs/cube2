package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.client.LiangcangPlatformLoginProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLoginInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/** 粮仓登录认证。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseAuthController {

    private final LiangcangPlatformLoginProvider platformLoginProvider;

    /** 获取平台账号密码登录 token 和租户信息。 */
    @PostMapping("/liangcang/warehouse-auth/login-info")
    public WarehouseLoginInfoVO loginInfo() {
        return platformLoginProvider.loginInfo();
    }
}
