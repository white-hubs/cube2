package com.guhecloud.cube.base.message2.client.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendVerifyCodeParam {
    @NotNull(message = "发送渠道不能为空")
    private ChannelEnum channel;
    @NotNull(message = "接收人不能为空")
    private Set<String> receivers;

}
