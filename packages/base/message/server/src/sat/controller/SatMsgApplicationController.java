package com.guhecloud.cube.base.message2.server.sat.controller;

import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgApplicationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 租户消息下的应用管理
 *
 * @author lanchun
 * @date 2023/6/26 16:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgApplicationController {

    private final SatMsgSceneReceiveService satMsgSceneReceiveService;

    /**
     * 租户消息应用列表
     *
     * @return
     */
    @GetMapping("/sat-msg-applications")
    public List<SatMsgApplicationVO> getSatMsgApplications() {
        return this.satMsgSceneReceiveService.getSatMsgSceneApplications();
    }
}
