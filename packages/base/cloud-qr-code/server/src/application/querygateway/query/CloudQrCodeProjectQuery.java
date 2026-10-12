package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云二维码项目查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudQrCodeProjectQuery extends Paging {

    /**
     * 关键词
     */
    private String keyword;
}
