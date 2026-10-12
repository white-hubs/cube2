package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 布局 VO
 */
@Data
public class SysSiteLayoutVO {

    private Long id;

    /** 布局名称 */
    private String layoutName;

    /** 布局配置JSON */
    private String config;

    /** 创建时间 */
    private LocalDateTime createdTime;

    /** 更新时间 */
    private LocalDateTime updatedTime;
}
