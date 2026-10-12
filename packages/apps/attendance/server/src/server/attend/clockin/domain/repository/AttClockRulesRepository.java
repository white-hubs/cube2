package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.aggregate.AttClockRulesAggregate;

/**
 * 请假规则仓储
 */
public interface AttClockRulesRepository {

    /**
     * 保存
     *
     * @param clockRulesAggregate 规则聚合
     */
    void insertClockRules(AttClockRulesAggregate clockRulesAggregate);

    /**
     * 修改
     *
     * @param clockRulesAggregate 规则聚合
     */
    void updateClockRules(AttClockRulesAggregate clockRulesAggregate);

    /**
     * 删除
     *
     * @param id id
     */
    void deleteClockRules(Long id);

}
