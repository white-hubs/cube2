package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cascade_tree")
public class CascadeTree {
    private Long id;
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
