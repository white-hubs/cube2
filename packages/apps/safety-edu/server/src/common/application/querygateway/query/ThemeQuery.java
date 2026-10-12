package com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class ThemeQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 租户编号
     */
    private Integer tenant;
}
