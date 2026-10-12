package com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 描述：维保对象 Repository接口
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Mapper
public interface MaintenanceObjectMapper extends BaseMapper<MaintenanceObject> {
    @Select("select key_id from maintenance_object where plan_id=#{planId}")
    Set<Long> selectAllByPlanId(@Param("planId") Long planId);

}