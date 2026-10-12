package com.guhecloud.cube.base.message2.server.handler.handler.send;

import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;

public interface CodeSender {
    default MessageSendResponse sendCode(MessageInfo messageInfo) {throw new UnsupportedOperationException();}

}
