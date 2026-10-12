package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.BroadcastPlayTextCommandService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCirculateCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextStopCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 广播播放相关接口
 *
 * @author lanchun
 * @date 2022/12/13 9:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayController {

    private final BroadcastPlayTextCommandService broadcastPlayTextCommandService;

    /**
     * 广播文本播放
     *
     * @param broadcastPlayTextCommand
     */
    @PostMapping("/broadcast-play/text")
    public void playText(@RequestBody @Valid BroadcastPlayTextCommand broadcastPlayTextCommand) {
        this.broadcastPlayTextCommandService.playText(broadcastPlayTextCommand);
    }

    /**
     * 广播文本循环播放
     *
     * @param broadcastPlayTextCirculateCommand
     */
    @PostMapping("/broadcast-play/circulate/text")
    public void playTextCirculate(@RequestBody @Valid BroadcastPlayTextCirculateCommand broadcastPlayTextCirculateCommand) {
        this.broadcastPlayTextCommandService.playTextCirculate(broadcastPlayTextCirculateCommand);
    }

    /**
     * 停止播放
     *
     * @param broadcastPlayTextStopCommand
     */
    @PostMapping("/broadcast-play/circulate/stop")
    public void playTextDeviceStop(@RequestBody @Valid BroadcastPlayTextStopCommand broadcastPlayTextStopCommand) {
        this.broadcastPlayTextCommandService.playTextDeviceStop(broadcastPlayTextStopCommand);
    }
}
