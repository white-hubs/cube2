package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/7 19:59
 * @Description:
 */
@Data
public class ApplicationBatchModifyParam {

    /**
     * id集合
     */
    private List<Long> applicationIds;

    /**
     * 是否显示
     */
    private Boolean enabled;

    /**
     * 是否公开访问
     */
    private Boolean publicAccess;

    /**
     * 应用分类
     */
    private Long applicationGroupId;

}
