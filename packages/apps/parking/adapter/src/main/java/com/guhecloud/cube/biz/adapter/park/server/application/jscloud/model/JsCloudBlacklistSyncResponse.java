package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑名单同步接口响应（新增/删除/查询）
 * code 为 0 表示成功
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBlacklistSyncResponse {

    /**
     * 响应码，0-成功
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据（查询时包含 totalCount、list 等）
     */
    private Object data;
}
