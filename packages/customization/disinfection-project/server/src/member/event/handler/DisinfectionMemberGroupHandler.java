package com.guhecloud.cube.srv.gen.disinfection.server.member.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupDeviceMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupUserMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupTotalEvent;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupHandler {
    private final DisinfectionMemberGroupMapper disinfectionMemberGroupMapper;
    private final DisinfectionMemberGroupUserMapper disinfectionMemberGroupUserMapper;
    private final DisinfectionMemberGroupDeviceMapper disinfectionMemberGroupDeviceMapper;

    @EventListener
    public void handleDisinfectionMemberGroupTotalEvent(final DisinfectionMemberGroupTotalEvent event) {
        Assert.isParameterError(event.getGroupId() == null, "分组不能为空");
        if (!event.isDeviceTotalUpdate() && !event.isUserTotalUpdate()) {
            log.warn("分组数量没有修改");
            return;
        }
        Integer userTotal = null;
        Integer deviceTotal = null;
        if (event.isUserTotalUpdate()) {
            userTotal = disinfectionMemberGroupUserMapper
                    .selectList(Wrappers.<MemberGroupUser>lambdaQuery().select(MemberGroupUser::getId)
                                        .eq(MemberGroupUser::getGroupId, event.getGroupId())).size();

        }
        if (event.isDeviceTotalUpdate()) {
            deviceTotal = disinfectionMemberGroupDeviceMapper
                    .selectList(Wrappers.<MemberGroupDevice>lambdaQuery().select(MemberGroupDevice::getId)
                                        .eq(MemberGroupDevice::getGroupId, event.getGroupId())).size();

        }
        disinfectionMemberGroupMapper.update(null, Wrappers.<MemberGroup>lambdaUpdate()
                .set(userTotal != null, MemberGroup::getUserCount, userTotal)
                .set(deviceTotal != null, MemberGroup::getDeviceCount, deviceTotal)
                .eq(MemberGroup::getId, event.getGroupId()));
    }
}
