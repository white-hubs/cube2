package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监管上报分页查询参数。
 *
 * <p>继承 Paging 中的 pageNumber 和 pageSize。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KeyPersonReportPageParam extends Paging {

    /**
     * 上报时间范围，格式示例：{@code (2026-01-01,2026-01-31)}、{@code (2026-01-01,)}、{@code (,2026-01-31)}。
     */
    private Period period;
}
