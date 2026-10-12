package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 应急设施管理
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyFacilityParam {

    //设施名称
    private String name;
    //设施类型编号
    private String facilityTypeCode;
    //设施类型名称
    private String facilityTypeName;
    //设施状态  USED 已使用 NOT_USED 未使用
    private String status;
    //备注
    private String remark;

}