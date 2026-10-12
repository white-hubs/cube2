package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AccountInfo {

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
     * 通讯号码(唯一)
     */
    private String number;

    /**
     * 账号密码
     */
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountInfo that = (AccountInfo) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
