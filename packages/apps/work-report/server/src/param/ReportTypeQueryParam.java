package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ReportTypeQueryParam extends Paging {
    private String keyword;
}
