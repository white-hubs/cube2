package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/4 14:07
 * @Description:
 */
@Data
public class FaultOrderPageQuery extends Paging {
    /**
     * 处理状态
     */
    private String status;

    /**
     * 发起时间
     */
    private Period submitTime;

    /**
     * 故障类型
     */
    private String faultTypeId;

    /**
     * 工单编号、工单名称、业务对象
     */
    private String keyword;
    @NotNull(message = "业务类型不能为空")
    private String businessType;

    /**
     * 处理进度
     */
    private String progress;

    /**
     * 处理结果
     */
    private String result;

    /**
     * 区域id列表
     */
    private List<Long> areaIds;
}
