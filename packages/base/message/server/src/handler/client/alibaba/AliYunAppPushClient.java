package com.guhecloud.cube.base.message2.server.handler.client.alibaba;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.push20160801.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;

public class AliYunAppPushClient {

    public static AsyncClient createClient(String accessKeyId, String accessKeySecret) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                                                                                    .accessKeyId(accessKeyId)
                                                                                    .accessKeySecret(accessKeySecret)
                                                                                    .build());
        // Configure the Client
        return AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("cloudpush.aliyuncs.com")
                                      )
                .build();
    }
}
