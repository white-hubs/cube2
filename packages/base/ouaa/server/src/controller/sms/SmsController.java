package com.guhecloud.cube.biz.ouaa.server.controller.sms;

import com.guhecloud.cube.biz.ouaa.server.param.sms.SendSmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信相关接口
 */
@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    /**
     * 发送验证码
     */
    @PostMapping("/sms/code/send")
    public Boolean sendSmsCode(@RequestBody SendSmsCodeParam sendSmsCodeParam) {
        return smsService.sendSmsCode(sendSmsCodeParam);
    }
}
