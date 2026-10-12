package com.guhecloud.cube.biz.linkage.server.rule.base.vo;

import com.guhecloud.cube.biz.linkage.server.linkage.vo.LinkageActionVO;
import lombok.Data;

import java.util.List;

/**
 * 规则详情返回信息
 *
 * @author lanchun
 * @date 2022/7/4 15:17
 */
@Data
public class RuleVO {

    /**
     * 规则基础信息
     */
    private RuleBasicVO ruleBasicVO;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 规则动作
     */
    private List<LinkageActionVO> linkageActionVOS;
}
