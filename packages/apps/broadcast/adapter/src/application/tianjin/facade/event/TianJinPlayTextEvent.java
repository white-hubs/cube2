package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.event;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.request.TianJinPlayTextRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class TianJinPlayTextEvent extends ApplicationEvent implements Supplier<String>, Consumer<String> {

    private final TianJinPlayTextRequest tianJinPlayTextRequest;

    private String resultCode;

    public TianJinPlayTextEvent(Object source,
                                TianJinPlayTextRequest tianJinPlayTextRequest
                               ) {
        super(source);
        this.tianJinPlayTextRequest = tianJinPlayTextRequest;
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
