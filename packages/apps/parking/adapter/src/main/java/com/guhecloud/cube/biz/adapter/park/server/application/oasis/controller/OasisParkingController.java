package com.guhecloud.cube.biz.adapter.park.server.application.oasis.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.oasis.service.OasisParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OasisParkingController {
    private final OasisParkingService oasisParkingService;

    @GetMapping("/oasis/parking-info")
    public List<ParkingInfoVO> getParkingInfo() {
        return oasisParkingService.getParkingInfo();
    }

    @GetMapping("/oasis/parking-in-records")
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return oasisParkingService.getParkingInRecords(param);
    }

    @GetMapping("/oasis/parking-out-records")
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return oasisParkingService.getParkingOutRecords(param);
    }

    @GetMapping("/oasis/parking-pay-records")
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return oasisParkingService.getParkingPayRecords(param);
    }
}
