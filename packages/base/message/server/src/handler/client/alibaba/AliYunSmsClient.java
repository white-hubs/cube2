package com.guhecloud.cube.base.message2.server.handler.client.alibaba;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;

public class AliYunSmsClient {

    public static AsyncClient createClient(String accessKeyId, String accessKeySecret) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                                                                                    // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
                                                                                    .accessKeyId(accessKeyId)
                                                                                    .accessKeySecret(accessKeySecret)
                                                                                    .build());
        return AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                                      )
                .build();
    }
}
