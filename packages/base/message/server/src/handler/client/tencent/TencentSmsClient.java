package com.guhecloud.cube.base.message2.server.handler.client.tencent;

import com.guhecloud.cube.base.message2.server.handler.domain.sms.TencentSmsAccountConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;

public class TencentSmsClient {

    public static SmsClient createClient(TencentSmsAccountConfig tencentSmsAccountConfig) {
        Credential cred = new Credential(tencentSmsAccountConfig.getSecurityId(), tencentSmsAccountConfig.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setConnTimeout(10); // 请求连接超时时间，单位为秒(默认60秒)
        httpProfile.setWriteTimeout(10);  // 设置写入超时时间，单位为秒(默认0秒)
        httpProfile.setReadTimeout(10);  // 设置读取超时时间，单位为秒(默认0秒)
        ClientProfile clientProfile = new ClientProfile();
        /* SDK默认用TC3-HMAC-SHA256进行签名
         * 非必要请不要修改这个字段 */
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        /* 实例化要请求产品(以sms为例)的client对象
         * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8 */
        return new SmsClient(cred, tencentSmsAccountConfig.getRegion(), clientProfile);
    }
}
