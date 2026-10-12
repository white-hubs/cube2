package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class GroupControlQueryParam extends Paging {
    private String keyword;
    private String ascription;
}
