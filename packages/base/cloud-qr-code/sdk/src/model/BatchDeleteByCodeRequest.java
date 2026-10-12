package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量通过code删除请求
 *
 * @author system
 */
@Data
public class BatchDeleteByCodeRequest {

    /**
     * 编码列表
     */
    private List<String> codes;
}
