package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:56
 * @Description:
 * @Version: 1.0
 */
public interface AttClockInLedgerRepository {
    AttClockInLedgerPO getClockInLedger(Long userId, String ledgerDate);

    Pagination<AttClockInLedgerResult> getClockInLedger(AttClockInLedgerPageParam param);

    List<AttClockInLedgerResult> getClockInLedgerList(AttClockInLedgerPageParam param);

    List<AttClockInLedgerDetailsResult> getClockInLedgerDetails(Long id);

    void addList(List<AttClockInLedgerPO> param);

    Boolean add(AttClockInLedgerPO param);

    Boolean updateOrgByUserId(Long userId, Long organizationId);
}
