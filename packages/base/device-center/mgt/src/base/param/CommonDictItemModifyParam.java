package com.guhecloud.cube.iot.mgt.base.param;

import lombok.Data;

@Data
public class CommonDictItemModifyParam {
    private Long id;
    private Long dictId;
    private Long parentId;
    private Integer sort;
    private String itemLabel;
    private String itemValue;
    private String remark;
}
