package com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenancePlanVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenancePlanQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：维保计划 Repository接口
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Mapper
public interface MaintenancePlanMapper extends BaseMapper<MaintenancePlan> {
    Page<MaintenancePlanVo> selectPlan(@Param("param") MaintenancePlanQueryParam param, Page page);

    List<Long> selectEnabledPlanIds(@Param("currentTime") String currentTime);

    void updatePlan(@Param("list") List<Long> list);

    Integer isUpkeepValid(@Param("deviceId") String deviceId);

    Integer isUpkeepTaskValid(@Param("deviceId") String deviceId, @Param("begin") String begin, @Param("end") String end);
}