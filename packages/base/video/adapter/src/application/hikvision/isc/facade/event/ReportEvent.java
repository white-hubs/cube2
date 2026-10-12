package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event;

import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReportEvent<R> extends ApplicationEvent implements Supplier<R>, Consumer<R> {

    private R result;

    public ReportEvent(Object source) {
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
