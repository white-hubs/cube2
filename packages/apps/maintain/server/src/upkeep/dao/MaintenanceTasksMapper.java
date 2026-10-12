package com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceProgetRecordCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceTasksVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceTasksQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：维保任务 Repository接口
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Mapper
public interface MaintenanceTasksMapper extends BaseMapper<MaintenanceTasks> {

    Page<MaintenanceTasksVo> selectTasks(@Param("param") MaintenanceTasksQueryParam param, Page page);

    Page<MaintenanceTasksVo> selectAppTasks(@Param("param") MaintenanceTasksQueryParam param, Page page);

    Integer selectAppTasksCount(@Param("param") MaintenanceTasksQueryParam param);

    @InterceptorIgnore(tenantLine = "on")
    MaintenanceTasksVo selectTasksDetail(@Param("id") Long id);

    Integer getTaskFlag(@Param("planId") Long planId);

    List<MaintenanceProjectVo> getObjectProjects(@Param("objectId") Long objectId);

    void updateProjectsRecord(@Param("list") List<MaintenanceProgetRecordCommand> list);

    List<DeviceOperationStatisticsDTO> deviceMaintenanceNumStatistics(@Param("param") DeviceStatisticParam param);
}