package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingPageParam;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/2 16:23
 * @Description:
 */
public interface AttSchedulingStaffService {
    Page<AttGroupStaffPO> getAttGroupStaffPOPage(AttSchedulingPageParam param);

    Page<AttSchedulingStaffPO> getAttSchedulingStaffPOPage(AttSchedulingPageParam param);

    List<AttGroupStaffPO> getAttGroupStaffPOS(AttSchedulingPageParam param);

    List<AttSchedulingStaffPO> getAttSchedulingStaffPOS(AttSchedulingPageParam param);

}
