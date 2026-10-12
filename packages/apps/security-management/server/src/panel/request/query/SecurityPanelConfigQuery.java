package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityPanelConfigQuery {

    /**
     * 业务主键集合
     */
    private Set<String> businessKeys;

    /**
     * 场景类型
     */
    private String sceneType;

    /**
     * 应用编号
     */
    private String applicationCode;

}
