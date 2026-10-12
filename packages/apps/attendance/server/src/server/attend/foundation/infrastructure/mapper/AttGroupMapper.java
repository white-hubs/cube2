package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupListResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AttGroupMapper extends BaseMapper<AttGroupPO> {

    @Select("select id,name from att_group ${ew.customSqlSegment} ")
    List<AttGroupListResult> selectGroupDropDown(@Param(Constants.WRAPPER) Wrapper<AttGroupPO> wrapper);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_group set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_group set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
