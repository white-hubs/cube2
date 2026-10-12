package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.InstanceListByGenreVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenancePlanVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenancePlanQueryParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 描述：维保计划服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
public interface MaintenancePlanService extends IService<MaintenancePlan> {

    Page<MaintenancePlanVo> selectPlan(MaintenancePlanQueryParam param);

    MaintenancePlanVo selectPlanDetail(Long id);

    Map<String, Object> getPlanAndTaskCount();

    List<InstanceListByGenreVo> instanceListByGenre();

    @GetMapping("/maintain/getPlanName/{id}")
    String getPlanName(@PathVariable Long id);
}