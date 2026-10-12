package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class WholesalerQueryParam extends Paging {
    //关键子
    private String keyword;
}
