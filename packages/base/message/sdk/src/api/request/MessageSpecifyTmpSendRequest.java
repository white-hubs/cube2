package com.guhecloud.cube.base.message.sdk.api.request;

import com.guhecloud.cube.base.message.sdk.common.annotation.Validation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 指定模板发送
 *
 * @author lanchun
 * @date 2023/6/28 9:34
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)// toString时，将也打印父类的属性，默认时false 的
public class MessageSpecifyTmpSendRequest extends MessageSceneSendRequest {

    /**
     * 发送通道信息(可指定通道使用具体的模板)
     */
    @Validation(required = true)
    private List<MessageChannelRequest> channel;
}
