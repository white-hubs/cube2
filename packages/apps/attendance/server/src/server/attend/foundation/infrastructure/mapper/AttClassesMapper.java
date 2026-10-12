package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesListResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 考勤班次mapper
 */
@Mapper
public interface AttClassesMapper extends BaseMapper<AttClassesPO> {

    @Select("select id,name,classes_type from att_classes ${ew.customSqlSegment} ")
    List<AttClassesListResult> selectClassDropDown(@Param(Constants.WRAPPER) Wrapper<AttClassesPO> wrapper);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_classes set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     *
     * @return 数量
     */
    @Update("update att_classes set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
