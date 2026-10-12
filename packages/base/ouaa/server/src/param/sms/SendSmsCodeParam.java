package com.guhecloud.cube.biz.ouaa.server.param.sms;

import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;

public record SendSmsCodeParam(String captchaVerification,String tel,SmsCodeTypeEnum sendSmsCodeType) {}
