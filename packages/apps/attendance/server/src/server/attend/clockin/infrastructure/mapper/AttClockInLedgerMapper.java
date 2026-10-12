package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsExcelResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:54
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface AttClockInLedgerMapper extends BaseMapper<AttClockInLedgerPO> {
    Page<AttClockInLedgerResult> getClockInLedger(@Param("page") Page page, @Param("param") AttClockInLedgerPageParam param);

    List<AttClockInLedgerResult> getClockInLedgerList(@Param("param") AttClockInLedgerPageParam param);

    List<AttClockInLedgerDetailsResult> getClockInLedgerDetails(@Param("id") Long id);

    List<AttClockInLedgerDetailsExcelResult> getClockInLogExcel(@Param("param") AttClockInLedgerPageParam param);

    List<AttClockInLedgerDetailsExcelResult> getLedgerDetailsExcel(@Param("param") AttClockInLedgerPageParam param,
                                                                   @Param("ledgerBelongDate") String ledgerBelongDate
                                                                  );

}
