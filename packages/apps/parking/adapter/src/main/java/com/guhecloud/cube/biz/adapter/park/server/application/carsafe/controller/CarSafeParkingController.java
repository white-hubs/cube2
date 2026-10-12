package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.model.CarSafeResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingChargeParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingInRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingOutRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.service.CarSafeParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 车安智慧停车场接口
 * 配置对面的服务器地址 ip:port/public/carsafe/
 *
 * @author xiaofeng
 * @date 2024/06/24 11:34
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarSafeParkingController {
    private final CarSafeParkingService carSafeParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/carsafe/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() {
        return this.carSafeParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/carsafe/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.carSafeParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/carsafe/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.carSafeParkingService.parkingUnSubscribe(param);
    }

    /**
     * 车安心跳接口
     *
     * @return
     */
    @PostMapping("/public/carsafe/{tenant}/report/parkHeartbeat")
    public CarSafeResponse parkHeartbeat(@RequestBody CarSafeParkingInRecordParam carSafeParkingInRecordParam,
                                         HttpServletRequest request
                                        ) {
        String ipAddr = request.getRemoteAddr();
        log.info("车安停车场，心跳IP地址:{}", ipAddr);
        return CarSafeResponse.builder().status(Boolean.TRUE).code(200).rowindex(0).total(0).build();
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/carsafe/{tenant}/report/parkInData")
    public CarSafeResponse reportParking2InRecord(@PathVariable Integer tenant,
                                                  @RequestBody CarSafeParkingInRecordParam carSafeParkingInRecordParam,
                                                  HttpServletRequest request
                                                 ) {
        String ipAddr = request.getRemoteAddr();
        log.info("车安停车场，进场调用者IP地址:{}", ipAddr);
        log.info("车安停车场，进场上报：{}", carSafeParkingInRecordParam);
        if (CollectionUtils.isEmpty(carSafeParkingInRecordParam.getData())) {
            throw new ServiceException("车安停车场，进场上报数据为空");
        }
        return this.carSafeParkingService.reportParkingInRecord(carSafeParkingInRecordParam.getData(), tenant);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/carsafe/{tenant}/report/parkOutdata")
    public CarSafeResponse reportParking2OutRecord(@PathVariable Integer tenant,
                                                   @RequestBody CarSafeParkingOutRecordParam carSafeParkingOutRecordParam,
                                                   HttpServletRequest request
                                                  ) {
        String ipAddr = request.getRemoteAddr();
        log.info("车安停车场，离场调用者IP地址：" + ipAddr);
        log.info("车安停车场，离场上报：" + carSafeParkingOutRecordParam);
        if (Objects.isNull(carSafeParkingOutRecordParam.getData())) {
            throw new ServiceException("车安停车场，离场上报数据为空");
        }
        return this.carSafeParkingService.reportParkingOutRecord(carSafeParkingOutRecordParam.getData(), tenant);
    }

    /**
     * 上报车场缴费记录
     *
     * @return
     */
    @PostMapping("/public/carsafe/{tenant}/report/parkTempCarCharge")
    public CarSafeResponse reportParking2ChargeRecord(@PathVariable Integer tenant,
                                                      @RequestBody CarSafeParkingChargeParam carSafeParkingChargeParam,
                                                      HttpServletRequest request
                                                     ) {
        String ipAddr = request.getRemoteAddr();
        log.info("车安停车场，缴费调用者IP地址：" + ipAddr);
        log.info("车安停车场，缴费记录上报：" + carSafeParkingChargeParam);
        if (Objects.isNull(carSafeParkingChargeParam.getData())) {
            throw new ServiceException("车安停车场，缴费上报数据为空");
        }
        return this.carSafeParkingService.reportParkingPayRecord(carSafeParkingChargeParam.getData(), tenant);
    }
}
