package com.guhecloud.cube.biz.mdata.server.grid.param;

import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 网格交接记录查询参数
 */
@Data
public class GridHandoverRecordQueryParam extends Paging {
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 交班时间范围
     */
    private Period period;

    /**
     * 交接状态
     */
    private GridHandoverStatusEnum status;

    /**
     * 交班人
     */
    private Long shiftOverUser;

    /**
     * 接班人
     */
    private Long takeOverUser;

    /**
     * 是否只查询当前所属用户
     */
    private Boolean permission;
}
