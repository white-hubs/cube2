package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 应急物资出入库记录
 * @date 2024-01-016 10:08:36
 */
@Data
public class EmergencySuppliesInoutRecordQueryParam extends Paging {

    //应急物资ID
    private Long suppliesId;
    //出入库记录号
    private String code;
    //出入库时间
    private Period period;
    //出入库类型
    private String inoutType;
    //出入库标签
    private String inoutTag;

}