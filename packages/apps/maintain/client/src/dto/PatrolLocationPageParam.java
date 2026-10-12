package com.guhecloud.cube.srv.gen.maintain.dto;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.Set;

@Data
public class PatrolLocationPageParam extends Paging {
    /**
     * 巡更点名称
     */
    private String keyword;
    /**
     * 所属区域
     */
    private String areaId;

    /**
     * id集合
     */
    private Set<Long> idSet;
}
