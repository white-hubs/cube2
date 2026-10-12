package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.cube.biz.linkage.client.param.LinkageActionModifyParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleBasicModifyParam;
import lombok.Data;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @author lrj
 * @Description 报警规则配置
 * @date 2021-12-30 15:12:36
 */
@Data
public class AlarmRulesParam {

    /**
     * 报警规则配置
     */
    private AlarmRulesBasicParam alarmRulesBasicParam;

    /**
     * 规则联动动作配置
     */
    @Valid
    private List<LinkageActionModifyParam> linkageEmergencyActionModifies;

    /**
     * 基础信息
     */
    @Valid
    private RuleBasicModifyParam basicModify;

}