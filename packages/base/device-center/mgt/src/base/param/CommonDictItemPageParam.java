package com.guhecloud.cube.iot.mgt.base.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class CommonDictItemPageParam extends Paging {

    /**
     * 字典类型
     */
    private Long dictId;

    /**
     * 字典项名称
     */
    private String itemLabel;

    /**
     * 包含子节点
     */
    private boolean withChildren;

    /**
     * 包含关联的字典项
     */
    private boolean withAssociatedItem;
}
