package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/10
 * @description 提交补卡规则
 */
@Data
public class SubmitSupplementCardRuleCommand {

    private Long id;

    /**
     * 名称
     */
    @Length(max = 20, message = "名称限制在20字内")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 允许补卡类型
     */
    @NotEmpty(message = "补卡类型必选")
    private List<String> types;

    /**
     * 每月允许补卡次数
     */
    private Integer times;

    /**
     * 几日内允许补卡
     */
    private Integer days;

}
