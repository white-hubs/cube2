package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 基础查询条件
 * @date 2023/9/22 15:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseQuery extends Paging {
    private Period period;
    private String keyword;
    private String type;
}
