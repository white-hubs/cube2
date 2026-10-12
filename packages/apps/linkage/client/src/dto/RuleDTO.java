package com.guhecloud.cube.biz.linkage.client.dto;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import lombok.Data;

import java.util.List;

/**
 * 规则详情返回信息
 *
 * @author lanchun
 * @date 2022/7/4 15:17
 */
@Data
public class RuleDTO {

    /**
     * 规则基础信息
     */
    private RuleBasicDTO ruleBasicVO;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 规则动作
     */
    private List<LinkageActionDTO> linkageActionVOS;
}
