package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

@Data
public class SysSitePublishConfigParam {
    /** 对象存储服务商，如 qiniu */
    private String provider;

    /** 对象存储配置JSON，包含 accessKey/secretKey/bucket/zone/domain */
    private String providerConfig;
}
