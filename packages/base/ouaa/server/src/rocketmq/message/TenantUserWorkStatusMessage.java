package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

import com.guhecloud.cube.biz.ouaa.server.dto.TenantUserWorkStatusDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserWorkStatusMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "TENANT-USER-WORK-STATUS-UPDATE";
    private Integer tenant;
    private List<TenantUserWorkStatusDTO> updateWorkStatusList;
}
