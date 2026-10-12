package com.guhecloud.cube.biz.ouaa.client.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户员工工作状态入参对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewWorkStatusParam {
    private Integer tenant;
    private Long userId;
    private String workStatus;
}
