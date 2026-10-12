package com.guhecloud.cube.iot.mgt.things.param;

import lombok.Data;

@Data
public class ProdDetailQueryParam {
    private Long prodId;
    private boolean withEvent;
    private boolean withProp;
}
