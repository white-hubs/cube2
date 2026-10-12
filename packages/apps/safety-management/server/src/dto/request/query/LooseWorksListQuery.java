package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wql
 * @describe 小散工程列表查询条件
 * @date 2023/9/19 15:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LooseWorksListQuery extends Paging {
    /**
     * 市场租户号
     */
    @NotBlank
    private Integer marketTenant;
    /**
     * 筛选时间
     */
    @NotBlank
    private Period period;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 监管部门
     */
    private Long depId;
    /**
     * 完成时间
     */
    private Period completeTime;
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 正常:NORMAL,异常:ABNORMAL,进行中 UNDERWAY
     */
    private String stateRange;
}
