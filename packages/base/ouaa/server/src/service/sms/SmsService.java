package com.guhecloud.cube.biz.ouaa.server.service.sms;

import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SendSmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.VerifySmsCodeParam;

public interface SmsService {

    Boolean sendSmsCode(SendSmsCodeParam sendSmsCodeParam);

    String verifySmsCode(VerifySmsCodeParam verifySmsCodeParam);

    void checkUserErrorCount(String mobile, SmsCodeTypeEnum smsCodeTypeEnum);

    void checkSmsAbility();

    String getAuthInfo(String tokenId);

    void removeAuthInfo(String tokenId);
}
