package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import lombok.Getter;

@Getter
public class ParkingPayRecordReportEvent extends ReportEvent<BasicResponse> {

    private final ParkingPayRecordReportRequest recordReportRequest;

    public ParkingPayRecordReportEvent(Object source, ParkingPayRecordReportRequest recordReportRequest) {
        super(source);
        this.recordReportRequest = recordReportRequest;
    }
}
