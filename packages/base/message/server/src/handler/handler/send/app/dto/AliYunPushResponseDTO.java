package com.guhecloud.cube.base.message2.server.handler.handler.send.app.dto;

import com.aliyun.sdk.service.push20160801.models.MassPushResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AliYunPushResponseDTO {

    private MassPushResponse androidPushResponse;

    private MassPushResponse iosPushResponse;
}
