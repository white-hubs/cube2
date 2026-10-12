package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 网站发布配置VO
 */
@Data
public class SysSitePublishConfigVO {

    private Long id;

    /** 对象存储服务商 */
    private String provider;

    /** 对象存储配置JSON */
    private String providerConfig;

}
