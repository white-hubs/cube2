package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingBaseQueryParam extends Paging {

    /**
     * 车场编号
     */
    protected String parkingInfoCode;

    /**
     * 区域主键
     */
    protected String areaId;

    /**
     * 车场编号详情
     */
    protected LinkedHashSet<String> parkingInfoCodes;

}
