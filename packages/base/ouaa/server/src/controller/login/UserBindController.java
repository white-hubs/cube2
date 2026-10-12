package com.guhecloud.cube.biz.ouaa.server.controller.login;

import com.guhecloud.cube.biz.ouaa.server.param.AuthBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsUnbindParam;
import com.guhecloud.cube.biz.ouaa.server.param.wechat.WechatBindParam;
import com.guhecloud.cube.biz.ouaa.server.service.UserBindService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户绑定手机和微信相关接口
 */
@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserBindController {
    private final UserBindService userBindService;

    /**
     * 根据临时code获取用户微信信息进行绑定
     */
    @PutMapping("/user/wechat/bind")
    public Boolean wechatBind(@RequestBody @Validated WechatBindParam param) {
        return userBindService.wechatBind(param.code());
    }

    /**
     * 根据临时code获取用户微信信息进行绑定
     */
    @PutMapping("/user/wechat/unbind")
    public Boolean wechatUnbind() {
        return userBindService.wechatUnbind();
    }

    /**
     * 手机号绑定
     */
    @PutMapping("/user/phone/bind")
    public Boolean phoneBind(@RequestBody @Validated SmsBindParam param) {
        return userBindService.phoneBind(param);
    }

    /**
     * 手机号解绑
     */
    @PutMapping("/user/phone/unbind")
    public Boolean phoneUnbind(@RequestBody @Validated SmsUnbindParam param) {
        return userBindService.phoneUnbind(param);
    }

    /**
     * 授权绑定三方账号
     */
    @PostMapping("/public/user/auth/bind-login")
    public Object authBindLogin(@RequestBody AuthBindParam param, HttpServletRequest request) {
        return userBindService.authBind(param, request);
    }
}

