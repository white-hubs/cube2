package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  14:27
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInLedgerPageParam extends Paging {
    /**
     * 日期
     */
    private String ledgerDate;
    /**
     * 人员姓名
     */
    private String userName;
    /**
     * 部门id
     */
    private Long orgId;
    private List<Long> userId;
}
