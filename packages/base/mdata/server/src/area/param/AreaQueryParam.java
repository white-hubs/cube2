package com.guhecloud.cube.biz.mdata.server.area.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

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
     * 主键集合
     */
    private Collection<Long> ids;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 空间状态id
     */
    private Long statusId;

    /**
     * 空间特征id
     */
    private Long featureId;

    /**
     * 空间标签id
     */
    private Long tagId;

    /**
     * 面积条件符号
     */
    private String areaSymbol;
    /**
     * 面积条件值
     */
    private Integer areaNumber;

    private List<String> sequences;
}
