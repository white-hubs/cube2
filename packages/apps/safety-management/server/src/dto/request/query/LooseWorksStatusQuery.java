package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author lijie
 * @describe 小散工程列表查询条件
 * @date 2024/12/24 17:31
 */
@Data
public class LooseWorksStatusQuery extends Paging {
    /**
     * 市场租户号
     */
    private List<Integer> marketTenant;
    /**
     * 筛选时间
     */
    @NotBlank
    private Period period;
    /**
     * 工程状态
     */
    private List<String> status;
}
