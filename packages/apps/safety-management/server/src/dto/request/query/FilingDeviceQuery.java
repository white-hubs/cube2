package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 维保到期设备查询条件
 * @date 2023/9/8 14:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FilingDeviceQuery extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    /**
     * 资产类别
     */
    private String type;
    private String subType;
    /**
     * 资产状态
     */
    private String state;
    /**
     * 入库日期
     */
    private Period putInDate;

    private Boolean expired;

    private String keyword;
}
