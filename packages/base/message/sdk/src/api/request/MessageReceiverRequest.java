package com.guhecloud.cube.base.message.sdk.api.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 指定接受者发送
 *
 * @author lanchun
 * @date 2023/10/16 10:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)// toString时，将也打印父类的属性，默认时false 的
public class MessageReceiverRequest extends MessageSceneSendRequest {

    /**
     * 接收通道
     */
    private List<MessageReceiverChannelRequest> receiverChannels;
}
