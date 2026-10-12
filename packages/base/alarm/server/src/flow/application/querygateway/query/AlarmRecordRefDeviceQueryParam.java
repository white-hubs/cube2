package com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 报警记录关联设备查询参数
 *
 * @author lanchun
 * @date 2025/6/26 14:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlarmRecordRefDeviceQueryParam extends Paging {

    /**
     * 报警记录id
     */
    @NotNull(message = "报警记录主见不能为空")
    private Long alarmRecordId;

    /**
     * 相当于原来的 model
     */
    private Long productId;

    /**
     * 品类ID
     */
    private String categoryCode;
}
