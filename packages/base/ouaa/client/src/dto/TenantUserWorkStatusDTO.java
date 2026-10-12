package com.guhecloud.cube.biz.ouaa.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserWorkStatusDTO {
    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 工作状态
     */
    private String workStatus;
}
