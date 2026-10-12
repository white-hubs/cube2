package com.guhecloud.cube.biz.adapter.park.server.application.jielink.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.jielink.model.JieLinkResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingPlaceReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.service.JieLinkParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * JieLink 3.*版本 停车场接口
 *
 * @author appet
 * @date 2023/10/11 16:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JieLinkParkingController {
    private final JieLinkParkingService jieLinkParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/jielink/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.jieLinkParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jielink/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jieLinkParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jielink/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jieLinkParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/jielink/report/{tenant}/carin")
    public JieLinkResponse reportParkingInRecord(@PathVariable Integer tenant,
                                                 @Valid @RequestBody JieLinkParkingInRecordReportParam param
                                                ) {
        log.info("jieLink停车场，上报车场入场记录：{}", param.toString());
        param.setTenant(tenant);
        return this.jieLinkParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/jielink/report/{tenant}/carout")
    public JieLinkResponse reportParkingOutRecord(@PathVariable Integer tenant,
                                                  @Valid @RequestBody JieLinkParkingOutRecordReportParam param
                                                 ) {
        log.info("jieLink停车场，上报车场出场记录：{}", param.toString());
        param.setTenant(tenant);
        this.jieLinkParkingService.reportParkingOutRecord(param);
        return this.jieLinkParkingService.reportParkingPayRecord(param);
    }

    /**
     * 车位数上报
     *
     * @param tenant
     * @param param
     *
     * @return
     */
    @PostMapping("/public/jielink/report/{tenant}/parktotalplace")
    public JieLinkResponse reportParkingPlace(@PathVariable Integer tenant,
                                              @Valid @RequestBody JieLinkParkingPlaceReportParam param
                                             ) {
        log.info("jieLink停车场，车位数上报：{}", param.toString());
        param.setTenant(tenant);
        return this.jieLinkParkingService.reportParkingPlace(param);
    }

    @PostMapping("/public/jielink/report/{tenant}/parktotalplace/{parkId}")
    public JieLinkParkingPlaceReportParam getParkingPlace(@PathVariable Integer tenant,
                                                          @PathVariable String parkId
                                                         ) {
        log.info("jieLink停车场，指定停车场车位数上报：{}", parkId);
        return this.jieLinkParkingService.getParkingPlaceFromRedis(tenant, parkId);
    }

}
