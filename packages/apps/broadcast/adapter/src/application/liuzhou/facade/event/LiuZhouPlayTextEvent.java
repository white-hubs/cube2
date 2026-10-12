package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.event;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.request.LiuZhouPlayTextRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class LiuZhouPlayTextEvent extends ApplicationEvent implements Supplier<String>, Consumer<String> {

    private final LiuZhouPlayTextRequest liuZhouPlayTextRequest;

    private String resultCode;

    public LiuZhouPlayTextEvent(Object source,
                                LiuZhouPlayTextRequest liuZhouPlayTextRequest
                               ) {
        super(source);
        this.liuZhouPlayTextRequest = liuZhouPlayTextRequest;
    }

    @Override
    public void accept(String s) {
        this.resultCode = s;
    }

    @Override
    public String get() {
        return this.resultCode;
    }
}
