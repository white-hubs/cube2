package com.guhecloud.cube.base.message2.client;

import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "message2")
public interface SatMsgSceneReceiveClient {

    /**
     * 消息场景接收配置详情
     *
     * @param code 场景编号
     *
     * @return
     */
    @GetMapping("/sat-msg-scene-receive/code/{code}")
    SatMsgSceneReceiveDTO getSatMsgSceneReceiveByCode(@PathVariable("code") String code);
}
