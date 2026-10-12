package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KeyWordPageParam extends Paging {
    private static final long serialVersionUID = 5003401228714431781L;
    private String keyword;
}
