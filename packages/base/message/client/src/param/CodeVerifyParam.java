package com.guhecloud.cube.base.message2.client.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeVerifyParam {
    @NotBlank(message = "receiver不能为空")
    private String receiver;

    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotNull(message = "渠道不能为空")
    private ChannelEnum channel;
}
