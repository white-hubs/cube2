package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 排班mapper
 */
@Mapper
public interface AttSchedulingMapper extends BaseMapper<AttSchedulingPO> {

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_scheduling set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     *
     * @return 数量
     */
    @Update("update att_scheduling set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Update("update att_scheduling set clock_read=true where id=#{id}")
    int updateSchedulingByClock(@Param("id") Long id);

}
