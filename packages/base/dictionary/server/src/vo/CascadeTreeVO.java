package com.guhecloud.cube.base.dictionary.server.vo;

import lombok.Data;

@Data
public class CascadeTreeVO {
    /**
     * 字典id
     */
    private Long dictionaryId;
    /**
     * 所有父级id
     */
    private String superiorsIds;
    /**
     * 所有子集id
     */
    private String nextIds;
    /**
     * 层级
     */
    private Integer level;
}
