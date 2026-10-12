package com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdFusionAccountVO {

    private static final long serialVersionUID = 1L;

    private Long id;

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
     * 账号密码
     */
    private String password;

    /**
     * 账号等级
     */
    private String level;

    /**
     * 账号使用状态
     */
    private String usageState;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
