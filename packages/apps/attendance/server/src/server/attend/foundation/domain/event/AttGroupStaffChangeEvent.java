package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.event;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 考勤组人员变更事件
 */
@Getter
public class AttGroupStaffChangeEvent extends ApplicationEvent {
    /**
     * 变更时的年月yyyy-MM
     */
    private final String yearMonth;
    /**
     * 变更的考勤组
     */
    private final AttGroupPO groupPO;
    /**
     * 入参的全量人员
     */
    private List<AttGroupStaffParam> allStaffParamList;
    /**
     * 考勤组新加的人员集合
     */
    private List<AttGroupStaffParam> addStaffParamList;
    /**
     * 移除的人员id
     */
    private List<Long> removeStaffIdList;
    // 目前只对新加的人有操作，如果以后对移除的人也有操作可以加移除的集合

    public AttGroupStaffChangeEvent(Object source, AttGroupPO groupPO, String yearMonth, List<AttGroupStaffParam> allStaffParamList,
                                    List<AttGroupStaffParam> addStaffParamList, List<Long> removeStaffIdList
                                   ) {
        super(source);
        this.groupPO = groupPO;
        this.allStaffParamList = allStaffParamList;
        this.addStaffParamList = addStaffParamList;
        this.yearMonth = yearMonth;
        this.removeStaffIdList = removeStaffIdList;
    }
}
