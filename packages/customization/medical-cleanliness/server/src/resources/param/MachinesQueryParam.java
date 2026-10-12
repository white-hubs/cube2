package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 机组查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MachinesQueryParam extends Paging {
    private Integer tenant;
    private Long id;
    private String code;
}
