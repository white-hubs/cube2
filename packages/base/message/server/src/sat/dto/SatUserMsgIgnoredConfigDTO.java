package com.guhecloud.cube.base.message2.server.sat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SatUserMsgIgnoredConfigDTO {

    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 是否为次日
     */
    private Boolean tomorrows;

    /**
     * 忽略的通道
     */
    private String channels;
}
