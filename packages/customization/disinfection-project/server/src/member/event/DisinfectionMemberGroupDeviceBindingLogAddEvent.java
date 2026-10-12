package com.guhecloud.cube.srv.gen.disinfection.server.member.event;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class DisinfectionMemberGroupDeviceBindingLogAddEvent extends ApplicationEvent {

    private final List<BindingLog> bindingLogs;

    public DisinfectionMemberGroupDeviceBindingLogAddEvent(Object source, List<BindingLog> bindingLogs) {
        super(source);
        this.bindingLogs = bindingLogs;
    }

    @Data
    public static class BindingLog {
        /**
         * 操作人账户
         */
        private final String account;
        /**
         * 组id
         */
        private final Long groupId;
        /**
         * 标题
         */
        private final Long deviceId;
    }
}
