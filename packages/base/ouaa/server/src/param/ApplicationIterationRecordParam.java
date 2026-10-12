package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 17:57
 * @Description:
 */
@Data
public class ApplicationIterationRecordParam {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 应用ID
     */
    private Long applicationId;

    /**
     * 应用版本号
     */
    private String version;

    /**
     * 上次迭代记录版本号
     */
    private String lastVersion;
    /**
     * 迭代内容
     */
    private List<ApplicationIterationContentInfo> contentList;
}
