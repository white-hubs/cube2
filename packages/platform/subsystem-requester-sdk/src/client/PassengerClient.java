package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.passenger.PassengerParam;
import com.guhecloud.subsystem.requester.sdk.vo.passenger.PassengerVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = SubsystemTypeConstants.PASSENGER_SERVER, configuration = DynamicConfiguration.class)
public interface PassengerClient {
    /**
     * @Description 客流记录
     */
    @PostMapping("/passenger-record")
    List<PassengerVo> getPassengerList(@RequestBody PassengerParam param);
}
