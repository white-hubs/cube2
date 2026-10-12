package com.guhecloud.cube.biz.fusion.communicate.server.gps.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGpsHistoriesQueryParam extends Paging {

    private static final long serialVersionUID = 1L;
    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 时间范围
     */
    private Period period;
}
