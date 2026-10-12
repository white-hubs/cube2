package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInLogResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttClockSchedulingClassesDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:20
 * @Description:
 * @Version: 1.0
 */
public interface AttClockInRecordsRepository extends IService<AttClockInRecordsPO> {
    /**
     * pc打卡记录
     *
     * @author: lijie
     * @date: 2023/9/26 16:59
     * @param:
     * @return:
     **/
    Pagination<ClockInLogResult> getClockInLog(ClockInLogPageParam param);

    /**
     * 某日打卡记录
     *
     * @author: lijie
     * @date: 2023/10/12 15:19
     * @param:
     * @return:
     **/
    List<AttClockInRecordsPO> getClockInLogByDate(ClockInLogDateParam param);

    List<AttClockInRecordsPO> getClockInLog(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询用户打卡信息
     */
    List<AttClockInRecordsPO> getClockInInfo(UserDTO user, AttClassesRecordPO classes, String uuid,
                                             AttClockSchedulingClassesDTO scheduling
                                            );

    /**
     * 根据uuid查询可以打上班卡还是下班卡
     */
    AttClockInRecordsPO getClockInInfo(String uuid, LocalDateTime actualClockInTime);

    AttClockInRecordsPO clockWork(AttClockInRecordsPO clockIn);

    AttClockInRecordsPO afterAlockWork(AttClockInRecordsPO clockIn);

    Boolean addClockLog(AttClockInRecordsPO attClockInRecordsPO);

    List<AttClockInRecordsPO> addClock(UserDTO user, AttClassesRecordPO classes, LocalDateTime classDate, Long schedulingId);

    AttClockInRecordsPO updateClockIn(Long id);

    /**
     * 补卡
     */
    AttClockInRecordsPO updateClockIn(String classesId);

    /**
     * 更新打卡记录状态为缺卡
     */
    Boolean updateClockInRecords(Long userId);

    Boolean updateClockInRecords(Long id, String clockType);

    /**
     * 修改班次同步修改未到上班时间但已打卡的范围
     */
    Boolean updateClockInByClasses(AttClassesRecordPO param);

    /**
     * 人脸识别失败重新写入验证结果
     */
    void revalidation(AttClockInRecordsPO clock);

    /**
     * 根据时间查找打卡记录
     */
    Period getAttClockInByDate(Period date);
}
