package com.guhecloud.cube.biz.subsystem.parking.client;

import com.guhecloud.cube.biz.subsystem.parking.client.param.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.ParkingInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 16:59
 * @Description: 停车场client
 */
@FeignClient(name = "subsystemParking")
public interface ParkingInfoClient {

    /**
     * 获取车场信息列表
     *
     * @return
     */
    @GetMapping("/parking-infos")
    List<ParkingInfoVO> getParkingInfos(@SpringQueryMap ParkingInfoQueryParam param);
}
