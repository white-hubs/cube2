package com.guhecloud.cube.base.message2.server.base.param;

import lombok.Data;

/**
 * 消息查询参数
 *
 * @author lanchun
 * @date 2021/11/16 17:13
 */
@Data
public class MsgRecordQueryByTenantParam extends MsgRecordQueryParam {
    /**
     * 租户编号
     */
    private Long tenant;
}
