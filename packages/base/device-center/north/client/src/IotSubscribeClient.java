package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.SubscribeResultDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.subscribe.SubscribeHttpParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotSubscribeClient {

    /**
     * HTTP订阅
     *
     * @param subscribeHttpParam 订阅参数
     * @return 订阅结果
     */
    @PostMapping("/north/device/subscribe")
    SubscribeResultDTO subscribe(@RequestBody SubscribeHttpParam subscribeHttpParam);

    /**
     * HTTP取消订阅
     *
     * @return 订阅结果
     */
    @PostMapping("/north/device/unsubscribe/{subscriptionId}")
    boolean unsubscribe(@PathVariable("subscriptionId") String subscriptionId);
}
