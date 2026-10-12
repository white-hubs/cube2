package com.guhecloud.cube.srv.gen.disinfection.server.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 消杀家庭组人员(设备)总数变更事件
 */
@Getter
public class DisinfectionMemberGroupTotalEvent extends ApplicationEvent {
    /**
     * 分组id
     */
    private final Long groupId;
    /**
     * 用户数量是否修改
     */
    private final boolean userTotalUpdate;
    /**
     * 设备数量是否修改
     */
    private final boolean deviceTotalUpdate;

    public DisinfectionMemberGroupTotalEvent(Object source, Long groupId, boolean userTotalUpdate, boolean deviceTotalUpdate) {
        super(source);
        this.groupId = groupId;
        this.userTotalUpdate = userTotalUpdate;
        this.deviceTotalUpdate = deviceTotalUpdate;
    }
}
