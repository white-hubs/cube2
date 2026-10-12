package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 请假台账PO
 * 请假单展开到每一天的数据
 * 例如：一条请假开始时间-请假结束时间为：2023-11-12 08:00:00 至 2023-11-15 18:00:00，请假时长为6小时的请假单
 * 会被拆分为 2023-11-12日,2023-11-13日,2023-11-14日,2023-11-15日的四条数据,每天请假时长平均分配为1.5小时
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_ask_leave_Ledger")
@EqualsAndHashCode(callSuper = true)
public class AttAskLeaveLedgerPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    private Long holidayId;
    private String holidayCode;
    // 请假时间
    private Long leaveNumber;
    // 请假日期
    private LocalDateTime leaveDate;
    // 请假单id
    private Long leaveFromId;
    // 一天折算多少小时
    private Integer dayOfHour;
    // 计入缺勤True表示计入
    private Boolean intoAbsence;
}
