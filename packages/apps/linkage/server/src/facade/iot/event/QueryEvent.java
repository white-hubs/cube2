package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueryEvent<R> extends ApplicationEvent implements Supplier<R>, Consumer<R> {
    protected R result;

    public QueryEvent(Object source) {
        super(source);
    }

    @Override
    public void accept(R r) {
        this.result = r;
    }

    @Override
    public R get() {
        return this.result;
    }
}
