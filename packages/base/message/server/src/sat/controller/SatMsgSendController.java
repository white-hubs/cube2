package com.guhecloud.cube.base.message2.server.sat.controller;

import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSendParam;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessController;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessModel;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台消息发送接口
 *
 * @author lanchun
 * @date 2021/11/15 18:38
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgSendController {
    private final ProcessController processController;

    /**
     * 消息发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-send/send")
    public Boolean sendMsg(@Valid @RequestBody SatMsgSendParam param) {
        MsgSendContext msgSendContext = MsgSendContext.builder()
                .satMsgSendParam(param)
                .build();
        ProcessContext<ProcessModel> processContext = ProcessContext.builder()
                .code("commandSendProcessTemplate")
                .processModel(msgSendContext)
                .build();
        processController.process(processContext);
        return Boolean.TRUE;
    }

}