package com.guhecloud.base.ocr.vehicle.server.third.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrVehicleRes {
    /**
     * 车牌号
     */
    private String vehicleNumber;
    /**
     * 车辆类型
     */
    private String vehicleType;
    /**
     * 额外信息
     */
    private Object extra;
}
