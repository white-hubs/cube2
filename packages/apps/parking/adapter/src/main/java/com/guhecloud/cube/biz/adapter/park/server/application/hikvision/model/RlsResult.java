package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.model;

import lombok.Data;

/**
 * @author wql
 * @describe 放行结果数据
 * @date 2024/3/5 17:51
 */
@Data
public class RlsResult {
    /**
     * 放行权限
     */
    private Integer releaseAuth;
    /**
     * 放行原因
     */
    private Integer releaseReason;
    /**
     * 放行结果
     */
    private Integer releaseResult;
    /**
     * 新体系放行结果
     */
    private Integer releaseResultEx;
    /**
     * 放行方式
     */
    private Integer releaseWay;
}
