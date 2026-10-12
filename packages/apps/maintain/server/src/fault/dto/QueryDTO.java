package com.guhecloud.cube.srv.gen.maintain.server.fault.dto;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryDTO extends Paging {
    private Integer num;
}
