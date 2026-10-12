package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenancePlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceUpdatePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.InstanceListByGenreVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenancePlanVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenancePlanService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenancePlanQueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lrj
 * @Description 维保计划
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenancePlanController {

    private final MaintenancePlanCommandService maintenancePlanCommandService;

    private final MaintenancePlanService maintenancePlanService;

    /**
     * @Description 获取计划状态枚举
     * @Author lrj
     */
    @GetMapping("/getStatus")
    public List<Map<String, Object>> getStatus() {
        return MaintenacePlanStatusEnum.toList();
    }

    /**
     * @Description 维保计划新增
     * @Author lrj
     */
    @PostMapping("/save-maintenance-plan")
    public void savePlan(@RequestBody MaintenancePlanCommand param) {
        maintenancePlanCommandService.savePlan(param);
    }

    /**
     * @Description 维保计划列表
     * @Author lrj
     */
    @GetMapping("/select-maintenance-plan")
    public Page<MaintenancePlanVo> selectPlan(MaintenancePlanQueryParam param) {
        return maintenancePlanService.selectPlan(param);
    }

    /**
     * @Description 维保计划详情
     * @Author lrj
     */
    @GetMapping("/select-maintenance-planDetail/{id}")
    public MaintenancePlanVo selectPlanDetail(@PathVariable Long id) {
        return maintenancePlanService.selectPlanDetail(id);
    }

    /**
     * @Description 维保计划启停
     * @Author lrj
     */
    @PutMapping("/update-maintenance-plan")
    public void updatePlan(@RequestBody MaintenanceUpdatePlanCommand param) {
        maintenancePlanCommandService.updatePlan(param);
    }

    /**
     * @Description 首页统计数
     * @Author lrj
     */
    @GetMapping("/get-PlanAndTaskCount")
    public Map<String, Object> getPlanAndTaskCount() {
        return maintenancePlanService.getPlanAndTaskCount();
    }

    /**
     * @Description 计划删除
     * @Author lrj
     */
    @DeleteMapping("/delete-plan/{id}")
    public void deletePlan(@PathVariable String id) {
        maintenancePlanCommandService.deletePlan(Long.valueOf(id));
    }

    /**
     * @Description 设备维保项转化
     * @Author lrj
     */
    @GetMapping("/instanceListByGenre")
    public List<InstanceListByGenreVo> instanceListByGenre() {
        //todo:设备维保项转化
        return maintenancePlanService.instanceListByGenre();
    }

    @GetMapping("/maintain/getPlanName/{id}")
    public String getPlanName(@PathVariable Long id) {
        return maintenancePlanService.getPlanName(id);
    }

}