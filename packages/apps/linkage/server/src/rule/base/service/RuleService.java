package com.guhecloud.cube.biz.linkage.server.rule.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleModifyParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleTriggerParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface RuleService extends IService<Rule> {

    /**
     * 规则新增
     *
     * @param ruleModifyParam
     *
     * @return
     */
    Long save(RuleModifyParam ruleModifyParam);

    /**
     * 规则详情
     *
     * @param id
     *
     * @return
     */
    Rule getById(Serializable id);

    /**
     * 规则编号查询
     *
     * @param code
     *
     * @return
     */
    Rule getByCode(String code);

    /**
     * id集合查询开启的规则
     *
     * @param idList
     *
     * @return
     */
    List<Rule> listEnabledRulesByIds(Collection<? extends Serializable> idList);

    /**
     * 规则修改
     *
     * @param id
     * @param ruleModifyParam
     */
    void update(Long id, RuleModifyParam ruleModifyParam);

    /**
     * 新增触发次数
     */
    void addTriggerNum(Long id);

    /**
     * 状态修改
     *
     * @param id
     * @param enabled
     */
    void updateEnabled(Long id, Boolean enabled);

    /**
     * 规则触发
     *
     * @param id
     * @param param
     */
    void triggerRule(Long id, RuleTriggerParam param);

    /**
     * 规则删除
     *
     * @param id
     */
    void remove(Long id);

    /**
     * 规则批量删除
     *
     * @param ids
     */
    void removeBatch(List<Long> ids);
}
