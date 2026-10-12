package com.guhecloud.cube.iot.mgt.core.data.north;

import lombok.Data;

/**
 * 订阅信息
 *
 * @author Fl
 * @date 2022-05-07 15:17
 **/
@Data
public class MsgEventSubscribeBo {
    private Long projectId;
    /**
     * 订阅条件
     */
    private String conditionJson;
    /**
     * 有效期
     */
    private Integer ttl;
    /**
     * 潜在优化点，不保证按字段优化订阅
     */
    private String propCodes;
}
