package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.controller;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.service.BaoLunV7700Service;
import com.guhecloud.cube.biz.adapter.broadcast.server.param.DevicePlayTextParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 保伦 itc v7700广播接口
 *
 * @author lanchun
 * @date 2024/11/7 11:55
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaoLunBroadcastV7700Controller {

    private final BaoLunV7700Service baoLunV7700Service;

    /**
     * 保伦广播设备文本播放
     *
     * @param param
     */
    @PostMapping("/baolun-v7700/device-play-text")
    public String devicePlayText(@RequestBody @Valid DevicePlayTextParam param) {
        return this.baoLunV7700Service.devicePlayText(param);
    }

    /**
     * 获取播放状态
     *
     * @param playTextId
     *
     * @return
     */
    @GetMapping("/baolun-v7700/device-play-text/state")
    public Integer devicePlayTextState(@RequestParam("playTextId") String playTextId) {
        return this.baoLunV7700Service.devicePlayTextState(playTextId);
    }

}
