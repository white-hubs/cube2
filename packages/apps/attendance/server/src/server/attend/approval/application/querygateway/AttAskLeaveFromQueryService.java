package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAskLeaveFromPageParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * 销假单查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveFromQueryService {

    private final AttAskLeaveFromMapper attAskLeaveFromMapper;

    /**
     * 请假单分页列表
     *
     * @param param 条件入参
     */
    public Pagination<AttAskLeaveFromPO> askLeaveFromPage(AttAskLeaveFromPageParam param) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (param.getSubmitTime() != null) {
            startTime = param.getSubmitTime().getBegin();
            endTime = param.getSubmitTime().getEnd();
        }
        Page<AttAskLeaveFromPO> page = attAskLeaveFromMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                .<AttAskLeaveFromPO>lambdaQuery()
                                                                                .eq(param.getStaffId() != null, AttAskLeaveFromPO::getApplicantId, param.getStaffId())
                                                                                .eq(StringUtils.isNotBlank(param.getStatus()), AttAskLeaveFromPO::getStatus, param.getStatus())
                                                                                .isNull(Boolean.TRUE.equals(param.getNotCancel()), AttAskLeaveFromPO::getCancelLeaveFromId)
                                                                                .le(startTime != null, AttAskLeaveFromPO::getCreateTime, startTime)
                                                                                .ge(endTime != null, AttAskLeaveFromPO::getCreateTime, endTime)
                                                                                .orderByDesc(AttAskLeaveFromPO::getLeaveStartTime)
                                                                       );
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }
}
