package com.guhecloud.cube.biz.meattrade.server.pork.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlaceStatQueryParam extends StatQueryParam {

    /**
     * 理货地点集合
     */
    private Set<String> placeIds;
}
