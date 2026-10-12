package com.guhecloud.cube.base.file.server.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2021/12/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileTenantInitMessage {

    public static final String TOPIC = "TENANT_INIT";

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
