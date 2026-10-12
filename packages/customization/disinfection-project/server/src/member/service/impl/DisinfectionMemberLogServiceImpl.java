package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.MemberLogMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberLogQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberLogService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberLogResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisinfectionMemberLogServiceImpl extends ServiceImpl<MemberLogMapper, MemberLog>
        implements DisinfectionMemberLogService {
    private final DisinfectionMemberGroupUserService memberGroupUserService;
    private final DisinfectionMemberGroupDeviceService memberGroupDeviceService;

    @Override
    public Pagination<MemberLogResult> getMemberLogPage(MemberLogQueryParam param) {
        MemberLogTypeEnum type = param.getType();
        var userId = SecurityContextHolder.getPrincipal().getId();
        var groupIds = memberGroupUserService.getGroupIdsByUserId(userId);
        if (CollectionUtils.isEmpty(groupIds)) {
            return Pagination.empty();
        }
        var memberGroupDevices = memberGroupDeviceService.lambdaQuery().select(MemberGroupDevice::getDeviceId)
                .in(MemberGroupDevice::getGroupId, groupIds).list();
        var deviceIds = memberGroupDevices.stream().map(MemberGroupDevice::getDeviceId).toList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Pagination.empty();
        }
        if (type.equals(MemberLogTypeEnum.DEVICE_ALARM_LOG)) {
            var pageResult = lambdaQuery().eq(MemberLog::getType, type).in(MemberLog::getDeviceId, deviceIds)
                    .page(PagingUtils.convert(param));
            return PagingUtils.convert(pageResult, MemberLogResult.class);
        } else if (type.equals(MemberLogTypeEnum.USER_OPERATION_LOG)) {
            var pageResult = lambdaQuery()
                    .eq(MemberLog::getType, type)
                    .in(MemberLog::getDeviceId, deviceIds)
                    .orderByDesc(MemberLog::getCreateTime)
                    .page(PagingUtils.convert(param));
            return PagingUtils.convert(pageResult, MemberLogResult.class);
        }
        // 构建分页查询条件
        Page<MemberLog> pageParam = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<MemberLog> pageResult = page(pageParam, lambdaQuery().getWrapper().eq(MemberLog::getType, type)
                .in(MemberLog::getDeviceId, deviceIds)
                .orderByAsc(MemberLog::getCreateTime));

        // 返回结果
        return PagingUtils.convert(pageResult, MemberLogResult.class);
    }
}
