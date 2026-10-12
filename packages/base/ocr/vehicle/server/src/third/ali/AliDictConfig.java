package com.guhecloud.base.ocr.vehicle.server.third.ali;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

@Data
public class AliDictConfig {
    private String accessKey;
    private String secretKey;
    private String region;
    private String endpoint;

    public void check() {
        Assert.isUnsupported(StringUtils.isBlank(accessKey), "阿里云配置accessKey未配置！");
        Assert.isUnsupported(StringUtils.isBlank(secretKey), "阿里云配置secretKey未配置！");
        Assert.isUnsupported(StringUtils.isBlank(region), "阿里云配置region未配置！");
        Assert.isUnsupported(StringUtils.isBlank(endpoint), "阿里云配置endpoint未配置！");
    }
}
