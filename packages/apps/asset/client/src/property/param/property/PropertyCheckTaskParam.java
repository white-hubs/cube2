package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class PropertyCheckTaskParam extends Paging {
    /**
     * 资产id
     */
    private Long propertyId;
    private Period period;
    /**
     * 盘点任务状态
     */
    private String status;

}
