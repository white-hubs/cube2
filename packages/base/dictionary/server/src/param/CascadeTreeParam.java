package com.guhecloud.cube.base.dictionary.server.param;

import lombok.Data;

@Data
public class CascadeTreeParam {
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
