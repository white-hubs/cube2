package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 应急设施管理
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyFacilityQueryParam extends Paging {

    //设施名称
    private String name;
    //设施类型编号
    private String facilityTypeCode;
    //设施类型名称
    private String facilityTypeName;
    //设施状态
    private String status;
    //备注
    private String remark;

}