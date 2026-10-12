package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.controller;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.service.TianJinBroadcastService;
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
 * 天津广播接口
 *
 * @author lanchun
 * @date 2022/12/8 20:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TianJinBroadcastController {

    private final TianJinBroadcastService tianJinBroadcastService;

    /**
     * 保伦广播设备文本播放
     *
     * @param param
     */
    @PostMapping("/tianjin/device-play-text")
    public String devicePlayText(@RequestBody @Valid DevicePlayTextParam param) {
        return this.tianJinBroadcastService.devicePlayText(param);
    }

    /**
     * 获取播放状态
     *
     * @param playTextId
     *
     * @return
     */
    @GetMapping("/tianjin/device-play-text/state")
    public Integer devicePlayTextState(@RequestParam("playTextId") String playTextId) {
        return this.tianJinBroadcastService.devicePlayTextState(playTextId);
    }

}
