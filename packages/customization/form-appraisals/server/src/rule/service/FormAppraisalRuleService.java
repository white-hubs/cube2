
package com.guhecloud.cube.srv.gen.appraisals.server.rule.service;

import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRuleCommand;

/**
 * 考核规则服务接口
 * 提供考核规则的增删改操作
 */
public interface FormAppraisalRuleService {
    /**
     * 保存考核规则
     *
     * @param command 考核规则保存命令
     * @return 新创建的规则 ID
     */
    Long save(FormAppraisalRuleCommand command);

    /**
     * 更新考核规则
     *
     * @param id      规则 ID
     * @param command 考核规则更新命令
     */
    void update(Long id, FormAppraisalRuleCommand command);

    /**
     * 启用或禁用考核规则
     *
     * @param id     规则 ID
     * @param enable 启用状态 true-启用 false-禁用
     */
    void enabled(Long id, Boolean enable);

    /**
     * 删除考核规则
     *
     * @param id 规则 ID
     */
    void remove(Long id);
}
