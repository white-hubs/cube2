package com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 描述：维保配置项 Repository接口
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Mapper
public interface MaintenanceProjectMapper extends BaseMapper<MaintenanceProject> {

    int countCategoryMaintenanceItemNum(@Param("categoryCode") String categoryCode);
}