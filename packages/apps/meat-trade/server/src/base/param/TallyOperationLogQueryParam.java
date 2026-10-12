package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 理货机运行查询参数
 *
 * @author lzy
 * @date 2021/9/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TallyOperationLogQueryParam extends Paging {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空")
    @Min(value = 1, message = "设备主键不能小于1")
    private Long deviceId;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
