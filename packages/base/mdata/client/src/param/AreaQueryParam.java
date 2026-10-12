package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;

/**
 * 区域查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 部门id
     */
    private String deptId;
    /**
     * 主键集合
     */
    private Collection<Long> ids;

    /**
     * 区域序列号集合
     */
    private List<String> sequences;
}
