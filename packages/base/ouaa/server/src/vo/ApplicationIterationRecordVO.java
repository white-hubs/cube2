package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationContentInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 18:15
 * @Description:
 */
@Data
public class ApplicationIterationRecordVO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 应用ID
     */
    private Long applicationId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 应用版本号
     */
    private String version;
    /**
     * 上次迭代记录版本号
     */
    private String lastVersion;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 迭代内容
     */
    private List<ApplicationIterationContentInfo> content;
}
