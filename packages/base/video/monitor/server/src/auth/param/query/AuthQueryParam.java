package com.guhecloud.cube.biz.vmonitor.server.auth.param.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;
}
