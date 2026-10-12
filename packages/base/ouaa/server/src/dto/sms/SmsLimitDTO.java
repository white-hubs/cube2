package com.guhecloud.cube.biz.ouaa.server.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsLimitDTO {
    /**
     * 最大发送次数
     */
    private Integer maxSendCount;
    /**
     * 最大发送次数时间范围
     */
    private Integer maxSendCountMinutesScope;
    private List<ErrorCountTimeLimitDTO> errorCountTimeLimits;

    public static SmsLimitDTO defaultLimit() {
        ArrayList<ErrorCountTimeLimitDTO> list = new ArrayList<>();

        list.add(ErrorCountTimeLimitDTO.builder().errorCount(5).minutes(5).build());
        return new SmsLimitDTO(5, 5, list);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorCountTimeLimitDTO {
        private Integer errorCount;
        private Integer minutes;
    }
}
