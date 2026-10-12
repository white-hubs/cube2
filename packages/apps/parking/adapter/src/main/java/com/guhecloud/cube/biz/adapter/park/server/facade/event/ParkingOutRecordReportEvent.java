package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import lombok.Getter;

@Getter
public class ParkingOutRecordReportEvent extends ReportEvent<BasicResponse> {

    private final ParkingOutRecordReportRequest recordReportRequest;

    public ParkingOutRecordReportEvent(Object source, ParkingOutRecordReportRequest recordReportRequest) {
        super(source);
        this.recordReportRequest = recordReportRequest;
    }
}
