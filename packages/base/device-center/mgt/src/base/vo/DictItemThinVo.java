package com.guhecloud.cube.iot.mgt.base.vo;

import lombok.Data;

@Data
public class DictItemThinVo {
    private Long id;
    private Long dictId;
    private Long itemParentId;
    private String itemLevel;
    private String itemLabel;
    private String itemValue;
    private int tenant;
}
