package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-27  16:58
 * @Description:
 * @Version: 1.0
 */
public interface AttClockInLedgerDetailsRepository {
    /**
     * @description: 生成某月的台账信息
     * @author: lijie
     * @date: 2023/10/7 9:25
     * @param: iedgerId 台账主表id
     * @param: date 台账时间
     * @return: 新增数量
     **/
    int addLedgerDetails(AttClockInLedgerPO iedger, String date);

    int addLedgerDetails(Long iedgerId, AttClockInRecordsPO attClockInRecords);

    /**
     * 更新休息的台账
     */
    Boolean updateRest();

    /**
     * 校验异常打卡状态
     */
    Boolean updateLedgerDetailsState();

    /**
     * 查询某个时间段内的旷工、迟到、早退次数
     */
    Integer getLedgerDetailsAbnormalNumber(AttClockInDetailsParam param);
}
