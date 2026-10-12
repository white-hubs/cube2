package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单接口对外返回结果
 * 只保留 code、msg、success 三个字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBwListResult {

    /**
     * 业务码（映射自公共错误码枚举）
     */
    private Integer code;

    /**
     * 中文提示信息
     */
    private String msg;

    /**
     * 是否成功
     */
    private Boolean success;
}

