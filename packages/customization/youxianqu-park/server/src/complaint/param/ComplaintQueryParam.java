package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.param;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.param.TrendQueryPageBaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投诉查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintQueryParam extends TrendQueryPageBaseParam {

    /**
     * 处理状态（ComplaintStatusEnum）
     */
    private String status;
}
