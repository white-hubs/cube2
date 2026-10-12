package com.guhecloud.cube.srv.gen.disinfection.server.member.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDeviceBindingLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupDeviceBindingLogAddEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceBindingLogService;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupDeviceBindingLogAddHandler {
    private final DisinfectionMemberGroupDeviceBindingLogService disinfectionMemberGroupDeviceBindingLogService;

    @EventListener
    public void disinfectionMemberLogAddEvent(final DisinfectionMemberGroupDeviceBindingLogAddEvent event) {
        List<DisinfectionMemberGroupDeviceBindingLogAddEvent.BindingLog> bindingLogs = event.getBindingLogs();
        if (ListUtils.isEmpty(bindingLogs)) {
            return;
        }

        //查询已经存在的设备id
        List<MemberGroupDeviceBindingLog> list = disinfectionMemberGroupDeviceBindingLogService.lambdaQuery()
                .select(MemberGroupDeviceBindingLog::getDeviceId).list();
        List<Long> existDeviceIds = ListUtils.convertItem(list, MemberGroupDeviceBindingLog::getDeviceId);
        //保留新的
        bindingLogs = ListUtils.removeIfNull(bindingLogs, log -> existDeviceIds.contains(log.getDeviceId()));
        List<MemberGroupDeviceBindingLog> bindingLogList = bindingLogs.stream().map(log -> {
            MemberGroupDeviceBindingLog memberGroupDeviceBindingLog = new MemberGroupDeviceBindingLog();
            memberGroupDeviceBindingLog.setId(IdWorker.getId());
            memberGroupDeviceBindingLog.setCreateBy(log.getAccount());
            memberGroupDeviceBindingLog.setGroupId(log.getGroupId());
            memberGroupDeviceBindingLog.setDeviceId(log.getDeviceId());
            return memberGroupDeviceBindingLog;
        }).collect(Collectors.toList());

        disinfectionMemberGroupDeviceBindingLogService.saveBatch(bindingLogList);
    }
}
