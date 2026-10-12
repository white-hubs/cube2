package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces.dto.BaiDuCallbackDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces.param.BaiDuClockInParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.message.DuMuMachineClockInMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-22  13:40
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceManageController {

    @PostMapping("/public/attCallback")
    public BaiDuCallbackDTO attCallback(@RequestBody BaiDuClockInParam param) {
        log.info("百度考勤机推送数据：{}", param.getUser_id());
        if (ObjectUtils.isNotEmpty(param.getUser_id())) {
            // 发送mq消息
            RocketMQContextHolder.send(DuMuMachineClockInMessage.TOPIC, DuMuMachineClockInMessage.builder()
                    .clockInfo(param)
                    .build());
        }
        return BaiDuCallbackDTO.builder()
                .code(0)
                .log("success")
                .build();
    }
}
