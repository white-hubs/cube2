package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.vo;

import lombok.Data;

@Data
public class SecurityPanelConfigVO {

    private Long id;

    /**
     * 业务主键(设备主键或产品等)
     */
    private String businessKey;

    /**
     * 使用场景(如安全设备列表,设备详情等)
     */
    private String sceneType;

    /**
     * 面板配置
     */
    private String panelConfig;

    /**
     * 所属应用
     */
    private String applicationCode;
}
