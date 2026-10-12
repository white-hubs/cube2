package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.Machines;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 机组
 */
@Mapper
public interface MachinesMapper extends BaseMapper<Machines> {

    /**
     * 批量插入机组
     * @param list
     */
    @InterceptorIgnore(tenantLine = "true")
    void insertMachinesBatch(@Param("list") List<Machines> list);

    /**
     * 获取全部机组
     * @return 机组集合
     */
    @Select("select id, code, tenant, room_ids from machines where deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    List<Machines> getMachinesAll();
}
