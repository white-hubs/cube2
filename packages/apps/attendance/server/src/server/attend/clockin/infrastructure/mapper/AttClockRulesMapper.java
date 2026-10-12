package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AttClockRulesMapper extends BaseMapper<AttClockRulePO> {
    AttClockRuleResult getClockRuleByUserId(@Param("userId") Long userId);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_clock_rule set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_clock_rule set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
