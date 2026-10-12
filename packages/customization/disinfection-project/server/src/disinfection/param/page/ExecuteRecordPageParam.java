package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExecuteRecordPageParam extends Paging {
    /**
     * 重点场所id
     */
    private Long keyPlaceId;
    private Period period;
    private String userName;
    /**
     * 默认只查询已完成的
     */
    private Boolean completed = Boolean.TRUE;
}
