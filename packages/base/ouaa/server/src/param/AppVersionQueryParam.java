package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AppVersionQueryParam extends Paging {

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 类型
     */
    private String type;
}
