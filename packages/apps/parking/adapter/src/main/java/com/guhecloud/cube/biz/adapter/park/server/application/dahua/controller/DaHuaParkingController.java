package com.guhecloud.cube.biz.adapter.park.server.application.dahua.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.dahua.service.DaHuaParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 大华停车场对接
 * 文档地址：<a href="https://open-icc.dahuatech.com/#/home?url=%3Fnav%3Dwiki%2Fipms%2Fparkinglot.html&version=enterprisebase/5.0.16&blank=true">...</a>
 */
@Slf4j
@RestController
@RequestMapping("/daHua")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaParkingController {

    private final DaHuaParkingService daHuaParkingService;

    /**
     * 获取车场信息
     *
     * @return 车场列表
     */
    @GetMapping("/parking-info")
    public List<ParkingInfoVO> getDaHuaParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.daHuaParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param 入参
     *
     * @return 返回
     */
    @PostMapping("/parking-subscribe")
    public Boolean subscribe(@RequestBody ParkingSubscribeParam param) {
        return daHuaParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param 入参
     *
     * @return 返回
     */
    @PostMapping("/parking-un-subscribe")
    Boolean unSubscribe(@RequestBody ParkingSubscribeParam param) {
        return daHuaParkingService.parkingUnSubscribe(param);
    }

    /**
     * 车场入场记录事件接收接口
     *
     * @return 上报结果
     */
    @PostMapping(value = "/public/{tenant}/parkingInRecord", consumes = "text/plain")
    public String daHuaParkingInRecord(HttpServletRequest request, @PathVariable Integer tenant,
                                       @RequestBody String requestBody
                                      ) {
        return daHuaParkingService.daHuaParkingInRecord(request, tenant, requestBody);
    }

    /**
     * 车场出场记录事件接收接口
     *
     * @return 上报结果
     */
    @PostMapping(value = "/public/{tenant}/parkingOutRecord", consumes = "text/plain")
    public String daHuaParkingOutRecord(HttpServletRequest request, @PathVariable Integer tenant,
                                        @RequestBody String requestBody
                                       ) {
        return daHuaParkingService.daHuaParkingOutRecord(request, tenant, requestBody);
    }
}
