package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.service.AttSchedulingStaffService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingPageParam;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/2 16:23
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingStaffServiceImpl implements AttSchedulingStaffService {
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingStaffMapper attSchedulingStaffMapper;

    @Override
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public Page<AttGroupStaffPO> getAttGroupStaffPOPage(AttSchedulingPageParam param) {
        return attGroupStaffMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                      .<AttGroupStaffPO>lambdaQuery()
                                                      .eq(param.getGroupId() != null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                      .like(StringUtils.isNotBlank(param.getKeyword()), AttGroupStaffPO::getName, param.getKeyword())
                                                      .orderByAsc(AttGroupStaffPO::getName)
                                             );
    }

    @Override
    @DataPermissionSwitch(dataModel = "attendance:arrangeWork")
    public Page<AttSchedulingStaffPO> getAttSchedulingStaffPOPage(AttSchedulingPageParam param) {
        return attSchedulingStaffMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                           .<AttSchedulingStaffPO>lambdaQuery()
                                                           .eq(AttSchedulingStaffPO::getSchedulingMonth, param.getYearMonth())
                                                           .eq(param.getGroupId() != null, AttSchedulingStaffPO::getGroupId, param.getGroupId())
                                                           .eq(StringUtils.isNotBlank(param.getKeyword()), AttSchedulingStaffPO::getStaffName, param.getKeyword())
                                                           .groupBy(AttSchedulingStaffPO::getStaffId)
                                                           .orderByAsc(AttSchedulingStaffPO::getStaffName)
                                                  );
    }

    @Override
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public List<AttGroupStaffPO> getAttGroupStaffPOS(AttSchedulingPageParam param) {
        return attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                      .eq(param.getGroupId() !=
                                                          null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                      .like(StringUtils.isNotBlank(param.getKeyword()), AttGroupStaffPO::getName, param.getKeyword())
                                                      .orderByAsc(AttGroupStaffPO::getName)
                                             );
    }

    @Override
    @DataPermissionSwitch(dataModel = "attendance:arrangeWork")
    public List<AttSchedulingStaffPO> getAttSchedulingStaffPOS(AttSchedulingPageParam param) {
        return attSchedulingStaffMapper.selectList(Wrappers.<AttSchedulingStaffPO>lambdaQuery()
                                                           .eq(AttSchedulingStaffPO::getSchedulingMonth, param.getYearMonth())
                                                           .eq(param.getGroupId() !=
                                                               null, AttSchedulingStaffPO::getGroupId, param.getGroupId())
                                                           .eq(StringUtils.isNotBlank(param.getKeyword()), AttSchedulingStaffPO::getStaffName, param.getKeyword())
                                                           .groupBy(AttSchedulingStaffPO::getStaffId)
                                                           .orderByAsc(AttSchedulingStaffPO::getStaffName)
                                                  );
    }
}
