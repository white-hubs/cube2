package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum DisinfectionTaskStatusEnum {

    WAIT_RECEIVE("待接收"),
    WAIT_EXECUTE("待执行"),
    EXECUTING("执行中"),
    FINISHED("已结束"),
    TERMINATED("已终止"),
    EXPIRED("已过期");
    private final String desc;

    public static List<String> getAppStatusList() {
        return Arrays.asList(WAIT_RECEIVE.name(), TERMINATED.name(), EXPIRED.name());
    }
}
