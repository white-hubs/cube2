package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class UserTenantQueryParam extends Paging {
    private Boolean enabled;
    private Boolean admin;
}
