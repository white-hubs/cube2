package com.guhecloud.cube.base.message2.server.sat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SatUserMsgIgnoredConfigVO {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 是否为次日
     */
    private Boolean tomorrows;

    /**
     * 忽略的通道
     */
    private Set<String> channels;
}
