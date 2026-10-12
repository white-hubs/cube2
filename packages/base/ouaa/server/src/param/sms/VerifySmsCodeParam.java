package com.guhecloud.cube.biz.ouaa.server.param.sms;

import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;

public record VerifySmsCodeParam(String tel, String code, SmsCodeTypeEnum verifySmsCodeType) {}
