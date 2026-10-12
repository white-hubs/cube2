package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author wql
 * @describe 隐患单列表查询参数
 * @date 2023/9/11 16:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HiddenDangerListQuery extends Paging {
    /**
     * 提交时间
     */
    private Period period;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    /**
     * 处理状态
     */
    private String status;
    /**
     * 隐患分类
     */
    private String category;

    /**
     * 过滤的(不包含)的隐患分类集合
     */
    private Set<String> filterCategories;
}
