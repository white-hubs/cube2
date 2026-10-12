package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportQuery extends Paging {
    private String keyword;
    private String type;
}
