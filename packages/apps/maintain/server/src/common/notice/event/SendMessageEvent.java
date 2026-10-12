package com.guhecloud.cube.srv.gen.maintain.server.common.notice.event;

import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/7/25 14:58
 * @description
 */
@Getter
public class SendMessageEvent extends ApplicationEvent {
    /**
     * 发送对象
     *
     * @param source
     */
    private List<MsgSendTargetParam> msgSendTargetParams;
    private Map<String, String> messageParam;
    private String code;
    private Long businessId;

    public SendMessageEvent(Object source, List<MsgSendTargetParam> msgSendTargetParams,
                            Map<String, String> messageParam,
                            String code,
                            Long businessId
                           ) {
        super(source);
        this.msgSendTargetParams = msgSendTargetParams;
        this.messageParam = messageParam;
        this.code = code;
        this.businessId = businessId;
    }

}
