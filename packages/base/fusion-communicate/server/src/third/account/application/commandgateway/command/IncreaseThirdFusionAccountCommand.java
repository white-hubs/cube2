package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseThirdFusionAccountCommand {

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 通讯号码
     */
    private String number;

    /**
     * 账号等级
     */
    private String level;

    /**
     * 密码
     */
    private String password;

    /**
     * 厂家编号
     */
    private String providerCode;
}
