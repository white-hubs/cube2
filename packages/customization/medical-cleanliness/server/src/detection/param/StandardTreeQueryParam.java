package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import lombok.Data;

import java.util.List;

@Data
public class StandardTreeQueryParam {
    /**
     * 产品id集合
     */
    private List<Long> productIdList;
}
