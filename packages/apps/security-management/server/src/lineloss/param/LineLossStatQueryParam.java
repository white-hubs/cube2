package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 线损统计查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossStatQueryParam extends Paging {
    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private String endTime;

    /**
     * 线路 ID
     */
    private Long lineId;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 设备 ID 集合（用于多设备查询）
     */
    private Set<Long> deviceIdSet;
}
