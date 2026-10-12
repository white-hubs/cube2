package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class LoginEvent extends ApplicationEvent implements Supplier<String>, Consumer<String> {

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * jsessionid
     */
    private String jsessionid;

    public LoginEvent(Object source, String username, String password) {
        super(source);
        this.username = username;
        this.password = password;
    }

    @Override
    public void accept(String s) {
        this.jsessionid = s;
    }

    @Override
    public String get() {
        return this.jsessionid;
    }
}
