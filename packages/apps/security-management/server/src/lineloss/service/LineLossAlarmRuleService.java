package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRulePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRulePageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;

public interface LineLossAlarmRuleService extends IService<LineLossAlarmRule> {

    /**
     * 根据 ID 查询详情
     *
     * @param id 规则 ID
     *
     * @return 规则详情
     */
    LineLossAlarmRuleVO detail(Long id);

    /**
     * 新增规则
     *
     * @param param 规则参数
     *
     * @return 是否成功
     */
    Long addRule(LineLossAlarmRuleParam param);

    /**
     * 修改规则
     *
     * @param param 规则参数
     *
     * @return 是否成功
     */
    boolean updateRule(LineLossAlarmRuleParam param);

    /**
     * 删除规则
     *
     * @param id 规则 ID
     *
     * @return 是否成功
     */
    boolean deleteRule(Long id);

    /**
     * 分页查询报警规则列表
     *
     * @param param 查询参数
     *
     * @return 分页列表
     */
    Pagination<LineLossAlarmRulePageVO> pageList(LineLossAlarmRulePageParam param);

    /**
     * 启用/禁用规则
     *
     * @param id      规则 ID
     * @param enable  true-启用，false-禁用
     *
     * @return 是否成功
     */
    boolean enableRule(Long id, Boolean enable);
}
