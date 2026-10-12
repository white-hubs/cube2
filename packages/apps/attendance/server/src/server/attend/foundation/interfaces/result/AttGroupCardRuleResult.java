package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttGroupCardRuleResult {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 允许补卡类型
     */
    private String type;

    /**
     * 允许补卡类型中文
     */
    private String typeName;

    /**
     * 每月允许补卡次数
     */
    private Integer times;

    /**
     * 几日内允许补卡
     */
    private Integer days;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 规则内容
     */
    private List<String> content;

    public void setType(String type) {
        this.type = type;
        if (StringUtils.isNotEmpty(type)) {
            this.typeName = ClockInEnum.getRemarksByName(type);
        }
    }
}
