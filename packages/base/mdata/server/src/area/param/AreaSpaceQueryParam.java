package com.guhecloud.cube.biz.mdata.server.area.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AreaSpaceQueryParam extends Paging {
    private Long id;
    private String name;
}
