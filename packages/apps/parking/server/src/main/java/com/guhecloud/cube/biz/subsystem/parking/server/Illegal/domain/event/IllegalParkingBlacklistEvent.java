package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.event;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.aggregates.IllegalParkingBlacklistInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:58
 * @Description: 违停黑名单事件
 */
@Getter
public class IllegalParkingBlacklistEvent extends ApplicationEvent {

    private final List<IllegalParkingBlacklistInfo> illegalParkingBlacklistInfoList;

    public IllegalParkingBlacklistEvent(Object source, List<IllegalParkingBlacklistInfo> illegalParkingBlacklistInfoList) {
        super(source);
        this.illegalParkingBlacklistInfoList = illegalParkingBlacklistInfoList;
    }
}
