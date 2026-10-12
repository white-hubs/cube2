package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:52
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInLedgerDetailsResult {
    private Long id;
    /**
     * 台账id
     */
    private Long iedgerId;
    /**
     * 周几
     */
    private String week;
    /**
     * 几号
     */
    private Integer today;
    private List<AttClockInLedger> details;
}
