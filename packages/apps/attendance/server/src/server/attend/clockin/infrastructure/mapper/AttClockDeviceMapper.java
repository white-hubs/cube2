package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockDevicePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

@Mapper
public interface AttClockDeviceMapper extends BaseMapper<AttClockDevicePO> {

    /**
     * 根据规则id删除
     *
     * @param time    时间戳
     * @param rulesId 规则id
     */
    @Update("update att_clock_device set deleted = #{time} where rules_id = #{rulesId}")
    void removeByRulesId(@Param("time") Long time, @Param("rulesId") Long rulesId);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_clock_device set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_clock_device set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 查询打卡地点
     *
     * @param wrapper 条件
     *
     * @return 集合
     */
    @Select("select device_id from att_clock_device ${ew.customSqlSegment}")
    Set<Long> selectDevicePo(@Param(Constants.WRAPPER) Wrapper<AttClockDevicePO> wrapper);
}
