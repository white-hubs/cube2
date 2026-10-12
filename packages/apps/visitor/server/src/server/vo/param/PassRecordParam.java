package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PassRecordParam extends Paging {
    private static final long serialVersionUID = -4774389408554741501L;
    private String name;
    private Period period;
}
