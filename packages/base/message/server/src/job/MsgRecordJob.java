package com.guhecloud.cube.base.message2.server.job;

import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息job
 *
 * @author lanchun
 * @date 2023/5/16 16:02
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordJob {

    private final MsgRecordService msgRecordService;

    public void expireMsgDel(String param) {
        //过期消息删除
        this.msgRecordService.removeExpireDel();
    }
}
