package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 考勤组人员mapper
 */
@Mapper
public interface AttGroupStaffMapper extends BaseMapper<AttGroupStaffPO> {
    /**
     * 逻辑删除
     *
     * @param time 时间戳
     * @param id   考勤组id
     */
    @Update("update att_group_staff set deleted = #{time} where group_id = #{groupId}")
    void removeByGroupId(@Param("time") Long time, @Param("groupId") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_group_staff set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
