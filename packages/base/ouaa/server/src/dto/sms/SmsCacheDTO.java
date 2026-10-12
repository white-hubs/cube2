package com.guhecloud.cube.biz.ouaa.server.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsCacheDTO {
    private String tel;
    private Long firstSendTime;
    private Long lastSendTime;
    private Long sendCount;
    private Long enableSendTime;
    private Long errorCount;
    private Long lastErrorLimitCount;
    private Long enableVerifyTime;

}
