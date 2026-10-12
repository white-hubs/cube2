package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttCancelLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 销假查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttCancelLeaveQueryService {

    /**
     * 销假校验
     *
     * @param param 销假单
     *
     * @return 是否通过
     */
    public Boolean cancelLeaveCheck(AttCancelLeaveFromDTO param, AttAskLeaveFromPO askLeaveFromPO) {

        if (param.getCancelStartTime() == null || param.getCancelEndTime() == null) {
            throw new ServiceException("销假时间不能为空！");
        }
        if (askLeaveFromPO != null && askLeaveFromPO.getLeaveStartTime() != null && askLeaveFromPO.getLeaveEndTime() != null) {
            if (!param.getCancelStartTime().equals(askLeaveFromPO.getLeaveStartTime()) &&
                !param.getCancelEndTime().equals(askLeaveFromPO.getLeaveEndTime())) {
                throw new ServiceException("不能销中间时间段的假期！");
            }
        } else {
            log.error("请假单请假时间为空：{}", askLeaveFromPO);
        }
        return true;
    }

}
