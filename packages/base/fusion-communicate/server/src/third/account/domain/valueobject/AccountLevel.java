package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject;

import lombok.Getter;

/**
 * 账号级别
 *
 * @author lanchun
 * @date 2024/7/29 15:22
 */
@Getter
public class AccountLevel {

    /**
     * 级别
     */
    private String level;

    public AccountLevel(String level) {
        this.level = level;
    }
}
