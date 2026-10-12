package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatrolRoutePageParam extends Paging {
    /**
     * 巡更路线名称
     */
    private String keyword;
}
