package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AlarmDeviceStatusParam extends Paging {
    /**
     * 时间范围
     */
    private String period;
    //关键字
    private String keyword;
}
