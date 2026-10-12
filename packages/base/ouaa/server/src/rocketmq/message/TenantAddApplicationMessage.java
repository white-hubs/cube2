package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 租户新增应用消息
 *
 * @author lanchun
 * @date 2023/7/4 9:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantAddApplicationMessage {

    public static final String TOPIC = "TENANT_ADD_APPLICATION";

    private Long id;

    /**
     * 应用编号列表
     */
    private List<String> applicationCodes;

    /**
     * 租户编号
     */
    private Integer tenant;
}
