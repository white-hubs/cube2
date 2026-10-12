package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeRuleVO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组加班规则
 */
@Data
public class AttendanceGroupWorkOvertimeRuleVO extends WorkOvertimeRuleVO {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 加班规则ID
     */
    private Long workOvertimeRuleId;

    private String config;

    public void setConfig(String config) {
        this.config = config;
        if (StringUtils.isNotEmpty(config)) {
            try {
                this.setRuleDetails(JSON.parseArray(config, WorkOvertimeRuleDetailVO.class));
            } catch (Exception e) {

            }
        }
    }
}