package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:55
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface AttClockInLedgerDetailsMapper extends BaseMapper<AttClockInLedgerDetailsPO> {
    int addLedgerDetails(@Param("list") List<AttClockInLedgerDetailsPO> list);

    int addLedgerDetailsAll(@Param("list") List<AttClockInLedgerDetailsPO> list);

    AttClockInLedgerDetailsPO getLedgerDetails(@Param("clock") AttClockInRecordsPO clock, @Param("ledgerDate") String ledgerDate,
                                               @Param("today") Integer today
                                              );

    List<AttClockInLedgerDetailsPO> getAll(@Param("ledgerDate") String ledgerDate);

    /**
     * 迟到、早退、旷工次数
     */
    Integer getLedgerDetailsAbnormalNumber(@Param("param") AttClockInDetailsParam param);
}
