package com.guhecloud.cube.biz.subsystem.parking.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 车辆查询入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleInfoQueryParam extends Paging {

    private Long id;
    /**
     * 车牌号码
     */
    private String vehicleNo;
    /**
     * 车辆类型编号
     */
    private String vehicleTypeCode;
    /**
     * 车辆类型名称
     */
    private String vehicleTypeName;
    /**
     * 车主姓名
     */
    private String customerName;
    /**
     * 车主联系手机号
     */
    private String customerMobile;
    private List<Long> idList;
}
