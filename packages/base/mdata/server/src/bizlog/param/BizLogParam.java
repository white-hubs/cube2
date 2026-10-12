package com.guhecloud.cube.biz.mdata.server.bizlog.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BizLogParam {
    private String type;

    private String typeName;

    private String subType;

    private String subTypeName;
}
