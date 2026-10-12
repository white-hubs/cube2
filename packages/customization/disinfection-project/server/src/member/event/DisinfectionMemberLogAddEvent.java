package com.guhecloud.cube.srv.gen.disinfection.server.member.event;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DisinfectionMemberLogAddEvent extends ApplicationEvent {
    /**
     * 操作人账户
     */
    private final String account;
    /**
     * 日志类型
     */
    private final MemberLogTypeEnum logType;
    private final Long deviceId;
    /**
     * 标题
     */
    private final String title;
    /**
     * 详情
     */
    private final String detail;

    /**
     * 操作日志事件
     *
     * @param source  源
     * @param account 操作人账户
     * @param logType 日志类型
     * @param title   标题
     * @param detail  详情
     */
    public DisinfectionMemberLogAddEvent(Object source, String account, MemberLogTypeEnum logType, Long deviceId, String title,
                                         String detail
                                        ) {
        super(source);
        this.account = account;
        this.logType = logType;
        this.deviceId = deviceId;
        this.title = title;
        this.detail = detail;
    }
}
