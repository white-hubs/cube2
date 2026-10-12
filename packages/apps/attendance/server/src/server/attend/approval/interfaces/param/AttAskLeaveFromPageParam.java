package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请假单入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttAskLeaveFromPageParam extends Paging {
    private Long id;
    private Long staffId;
    private String keyword;
    private String status;
    /**
     * 未销假
     */
    private Boolean notCancel;

    private Period submitTime;
}
