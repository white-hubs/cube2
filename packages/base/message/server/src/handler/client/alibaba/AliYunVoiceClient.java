package com.guhecloud.cube.base.message2.server.handler.client.alibaba;

import com.aliyun.dyvmsapi20170525.Client;

public class AliYunVoiceClient {

    public static Client createClient(String accessKeyId, String accessKeySecret) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dyvmsapi.aliyuncs.com";
        try {
            return new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
