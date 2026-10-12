package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatrolLocationPageParam extends Paging {
    private Set<Long> idSet;
    /**
     * 巡更点名称
     */
    private String keyword;
    /**
     * 所属区域
     */
    private String areaId;
}
