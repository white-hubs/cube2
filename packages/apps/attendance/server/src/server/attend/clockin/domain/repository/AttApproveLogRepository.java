package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import com.guhecloud.platform.base.model.Period;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-01  09:56
 * @Description:
 * @Version: 1.0
 */
public interface AttApproveLogRepository extends IService<AttApproveLogPO> {
    /**
     * 是否有审批
     */
    Boolean getApproveLogIsData(String date);

    /**
     * 查询所有审批
     */
    List<AttApproveLogPO> getApproveLogAllByDate(String date);

    /**
     * 新增审批
     */
    Boolean addApproveLog(List<AttApproveLogPO> attApproveLog);

    /**
     * 修改审批结果
     */
    Boolean updateApproveLog(String status, String processInstanceId);

    Boolean updateApproveLog(String status, String remarks, String processInstanceId);

    Boolean updateApproveLog(AttApproveLogPO log);

    /**
     * 根据流引擎实例id查询审批
     */
    List<AttApproveLogPO> getApproveLog(String processInstanceId);

    /**
     * 查询用户某个时间是否有请假、离岗、出差
     */
    Boolean getApproveLogByUser(LocalDateTime clockInTime, Long userId);

    /**
     * 查询用户某个时间已通过的请假、出差、离岗审批
     */
    List<AttApproveLogPO> getApproveLog(Long userId, LocalDateTime clockInTime);

    List<AttApproveLogPO> getApproveLogByUserAndTime(AttClockInDetailsParam param);

    /**
     * 是否存在申请,存在返回时间段，不存在返回null
     *
     * @param userId       人员id
     * @param startTime    开始时间(只要有交叉就算存在)
     * @param endTime      结束时间(只要有交叉就算存在)
     * @param businessType 申请类型(请假/离岗/加班/补卡...)
     * @param sceneCode    场景code(请假时为假期类型id)
     * @param statusList   状态集合(in条件)
     *
     * @return 存在返回时间段，不存在返回null
     */
    Period existApproveLog(Long userId, LocalDateTime startTime, LocalDateTime endTime, String businessType, String sceneCode,
                           List<String> statusList
                          );
}
