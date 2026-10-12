package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量通过code查询请求
 */
@Data
public class BatchQueryByCodeRequest {
    /**
     * code列表
     */
    private List<String> codes;
}
