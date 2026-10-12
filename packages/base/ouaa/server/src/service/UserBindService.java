package com.guhecloud.cube.biz.ouaa.server.service;

import com.guhecloud.cube.biz.ouaa.server.param.AuthBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsBindParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SmsUnbindParam;
import jakarta.servlet.http.HttpServletRequest;

public interface UserBindService {
    Boolean wechatBind(String code);

    Boolean wechatUnbind();

    Boolean phoneBind(SmsBindParam param);

    Boolean phoneUnbind(SmsUnbindParam param);

    Object authBind(AuthBindParam param, HttpServletRequest request);
}
