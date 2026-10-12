package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/10
 * @description 补卡规则
 */
@Data
public class SupplementCardRuleVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 允许补卡类型
     */
    private List<String> types;

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

    public void setTypes(List<String> types) {
        this.types = types;
        if (ObjectUtils.isNotEmpty(types)) {
            for (String ex : types) {
                if (ObjectUtils.isEmpty(this.typeName)) {
                    this.typeName = ClockInEnum.getRemarksByName(ex);
                } else {
                    this.typeName = this.typeName + "、" + ClockInEnum.getRemarksByName(ex);
                }
            }
        }
    }
}