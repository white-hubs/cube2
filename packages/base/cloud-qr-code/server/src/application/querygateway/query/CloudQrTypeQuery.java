package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CloudQrTypeQuery extends Paging {

    private String keyword;

    private String grantClientId;
}
