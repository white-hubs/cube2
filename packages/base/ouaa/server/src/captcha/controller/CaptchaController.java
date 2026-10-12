package com.guhecloud.cube.biz.ouaa.server.captcha.controller;

import com.guhecloud.cube.biz.ouaa.server.captcha.model.common.ResponseModel;
import com.guhecloud.cube.biz.ouaa.server.captcha.model.vo.CaptchaVO;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaService;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get")
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    @PostMapping("/check")
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return ipList[0].trim();
        }
        return null;
    }

}
