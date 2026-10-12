package com.guhecloud.cube.biz.linkage.server.linkage.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 事件查询参数
 *
 * @author lanchun
 * @date 2022/6/29 19:49
 */
@Data
public class EventQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private String businessType;

}
