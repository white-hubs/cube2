package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.broadcast.DevicePlayTextParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 广播
 *
 * @author lanchun
 * @date 2022/12/7 15:48
 */
@FeignClient(name = SubsystemTypeConstants.BROADCAST_SERVER, configuration = DynamicConfiguration.class)
public interface BroadcastClient {

    /**
     * 保伦广播设备文本播放
     *
     * @param param
     */
    @PostMapping("/device-play-text")
    String devicePlayText(@RequestBody @Valid DevicePlayTextParam param);

    /**
     * 获取广播播放状态
     *
     * @param playTextId
     *
     * @return
     */
    @GetMapping("/device-play-text/state")
    Integer devicePlayTextState(@RequestParam("playTextId") String playTextId);
}
