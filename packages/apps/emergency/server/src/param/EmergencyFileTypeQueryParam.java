package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class EmergencyFileTypeQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;
}
