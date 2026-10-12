package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("holiday_grant_rule")
@AllArgsConstructor
@NoArgsConstructor
public class HolidayGrantRulePO {
    private Long id;
    /**
     * 发放方式
     */
    private String grantWay;
    /**
     * 发放时间
     */
    private String grantTime;
    /**
     * 额度配置
     */
    private String quota;
    /**
     * 有效期配置
     */
    private String expiryDate;
}
