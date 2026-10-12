package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatrolDeviceParam extends Paging {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 所属区域
     */
    private String areaId;
}
