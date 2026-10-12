package com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class QrcodeQueryParam extends Paging {

    private String keyword;

    private String entityType;

}
