package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import lombok.Getter;

@Getter
public class ParkingInRecordReportEvent extends ReportEvent<BasicResponse> {

    private final ParkingInRecordReportRequest recordReportRequest;

    public ParkingInRecordReportEvent(Object source, ParkingInRecordReportRequest recordReportRequest) {
        super(source);
        this.recordReportRequest = recordReportRequest;
    }
}
