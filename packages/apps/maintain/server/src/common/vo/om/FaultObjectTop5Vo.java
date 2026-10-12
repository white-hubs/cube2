package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-04 15:11
 */
@Data
@Builder
public class FaultObjectTop5Vo {
    /**
     * 故障对象
     */
    private String faultObject;
    /**
     * 故障数量
     */
    private Long faultNum;

}
