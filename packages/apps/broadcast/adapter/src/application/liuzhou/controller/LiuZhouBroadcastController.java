package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.controller;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.service.LiuZhouBroadcastService;
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
 * 柳州广播接口
 *
 * @author lanchun
 * @date 2024/12/12 20:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiuZhouBroadcastController {

    private final LiuZhouBroadcastService liuZhouBroadcastService;

    /**
     * 保伦广播设备文本播放
     *
     * @param param
     */
    @PostMapping("/liuzhou/device-play-text")
    public String devicePlayText(@RequestBody @Valid DevicePlayTextParam param) {
        return this.liuZhouBroadcastService.devicePlayText(param);
    }

    /**
     * 获取播放状态
     *
     * @param playTextId
     *
     * @return
     */
    @GetMapping("/liuzhou/device-play-text/state")
    public Integer devicePlayTextState(@RequestParam("playTextId") String playTextId) {
        return this.liuZhouBroadcastService.devicePlayTextState(playTextId);
    }

}
