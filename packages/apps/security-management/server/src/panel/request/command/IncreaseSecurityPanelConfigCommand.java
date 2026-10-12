package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

@Data
public class IncreaseSecurityPanelConfigCommand {

    /**
     * 业务主键(设备主键或产品等)
     */
    @NotEmpty(message = "绑定面板的主业务信息不能为空")
    @Length(max = 50, message = "业务主键不能大于50")
    private String businessKey;

    /**
     * 使用场景(如安全设备列表,设备详情等)
     */
    @Length(max = 50, message = "使用场景不能大于50")
    private String sceneType;

    /**
     * 面板配置
     */
    private String panelConfig;

    /**
     * 所属应用
     */
    @Length(max = 50, message = "所属应用不能大于50")
    private String applicationCode;
}
