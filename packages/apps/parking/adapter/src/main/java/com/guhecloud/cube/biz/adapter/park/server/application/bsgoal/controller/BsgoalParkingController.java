package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.service.BsgoalParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 博思高停车场接口(出入记录未提供推送)
 *
 * @author xiaofeng
 * @date 2024/11/15 11:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BsgoalParkingController {
    private final BsgoalParkingService bsgoalParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/bsgoal/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() {
        return this.bsgoalParkingService.getParkingInfo();
    }

    /**
     * 获取车辆入场记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/bsgoal/parking-in-records")
    List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return this.bsgoalParkingService.getParkingInRecords(param);
    }

    /**
     * 获取车辆出场记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/bsgoal/parking-out-records")
    List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return this.bsgoalParkingService.getParkingOutRecords(param);
    }

    /**
     * 获取车辆缴费记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/bsgoal/parking-pay-records")
    List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return this.bsgoalParkingService.getParkingPayRecords(param);
    }
}
