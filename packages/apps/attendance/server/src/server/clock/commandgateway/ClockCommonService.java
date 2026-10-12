package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/12/06
 * @description
 */
public interface ClockCommonService {

    /**
     * 更新没有打卡的为缺卡（没有超出下班打卡范围的不更新）
     *
     * @param vo             需要更新的当前班次
     * @param currentDate    当前时间
     * @param staffDetailDTO 当前员工信息
     */
    void updateClockToNoCard(CurrentClassesVO vo, LocalDateTime currentDate, StaffDetailDTO staffDetailDTO);

    /**
     * 更新上个班次的打卡结果为缺卡
     *
     * @param staffD 员工
     * @param vo     当前班次
     */
    void updateLastClockToNoCard(StaffDetailDTO staffD, CurrentClassesVO vo);

    void updateClockToNoCardJob(CurrentClassesVO vo, LocalDateTime currentDate, StaffDetailDTO staffDetailDTO);
}
