package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttGroupPageParam extends Paging {
    private Long id;
    private String name;
}
