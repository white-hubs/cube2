package com.guhecloud.cube.iot.mgt.north.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ItemVo {
    @JSONField(serialize = false)
    @JsonIgnore
    private Long itemId;
    @JSONField(serialize = false)
    @JsonIgnore
    private Long itemParentId;
    private String itemLabel;
    private String itemCode;
    private List<ItemVo> childrenList;
}
