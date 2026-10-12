package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query;

import lombok.Data;

import java.util.Set;

@Data
public class CloudQrTypeBatchQuery {

    private Set<String> codes;
}
