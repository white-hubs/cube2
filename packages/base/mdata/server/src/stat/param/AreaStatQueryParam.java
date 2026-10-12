package com.guhecloud.cube.biz.mdata.server.stat.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.Collection;

/**
 * @author lzy
 * @date 2022/4/19
 */
@Data
public class AreaStatQueryParam extends Paging {

    /**
     * 区域组主键
     */
    private Long areaGroupId;

    /**
     * 区域信息
     */
    private Collection<String> areas;

}
