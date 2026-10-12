package com.guhecloud.cube.biz.daily.task.domain.adapter.dto;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lzy
 * @date 2021/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaDTO extends Tree.Simple<Long, AreaDTO> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 是否启用
     */
    private Boolean enabled;

    private String areaManagerId;

    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;
    private String typeId;
    private String typeName;
    private List<Long> deptIds;
    private String deptNames;
}
