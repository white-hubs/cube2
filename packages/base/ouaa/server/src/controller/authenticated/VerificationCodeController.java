package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.guhecloud.cube.biz.ouaa.server.dto.ImgCodeDTO;
import com.guhecloud.platform.base.util.StringUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.biz.ouaa.server.service.VerificationCodeService.PWD_CODE_KEY;

/**
 * @author pengjian
 * @date 2022/10/9 11:25
 * @description
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/public/auth/login/code")
public class VerificationCodeController {
    private final RedisTemplate redisTemplate;

    /**
     * 获取Kaptcha验证码，返回前端图片流
     */
    @GetMapping("/generate")
    public ImgCodeDTO kaptchaImg(String key) throws IOException, FontFormatException {
        // 算数验证码
        Captcha captcha = new SpecCaptcha();
        captcha.setLen(4);
        captcha.setFont(Captcha.FONT_2);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 生成的验证码
        String text = captcha.text();
        System.out.println("验证码 = " + text);

        // 写入redis,前端登录的时候带key请求
        if (StringUtils.isEmpty(key)) {
            key = String.valueOf(System.currentTimeMillis());
        }

        redisTemplate.opsForValue().set(PWD_CODE_KEY + key, text);
        redisTemplate.expire(PWD_CODE_KEY + key, 3, TimeUnit.MINUTES);

        return new ImgCodeDTO(key, captcha.toBase64());
    }

}
