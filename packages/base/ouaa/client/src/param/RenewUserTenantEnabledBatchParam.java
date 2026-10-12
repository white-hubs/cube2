package com.guhecloud.cube.biz.ouaa.client.param;

import lombok.Data;

import java.util.List;

/**
 * 批量更新租户人员启停状态
 */
@Data
public class RenewUserTenantEnabledBatchParam {
    private Integer tenant;
    private List<Long> enableUserIds;
    private List<Long> disableUserIds;
}
