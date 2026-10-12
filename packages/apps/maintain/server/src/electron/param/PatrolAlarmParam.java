package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatrolAlarmParam extends Paging {
    /**
     * 关键字
     */
    private String keyword;
}
