package com.guhecloud.cube.srv.gen.disinfection.server.member.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberLogAddEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberLogAddHandler {
    private final DisinfectionMemberLogService disinfectionMemberLogService;

    @EventListener
    public void disinfectionMemberLogAddEvent(final DisinfectionMemberLogAddEvent event) {
        MemberLog memberLog = new MemberLog();
        memberLog.setId(IdWorker.getId());
        memberLog.setCreateBy(event.getAccount());
        memberLog.setDeviceId(event.getDeviceId());
        memberLog.setType(event.getLogType());
        memberLog.setTitle(event.getTitle());
        memberLog.setDetail(event.getDetail());
        memberLog.setCreateTime(LocalDateTime.now());
        disinfectionMemberLogService.save(memberLog);
    }
}
