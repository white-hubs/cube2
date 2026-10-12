package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.plan;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanStatusParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
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

/**
 * 巡更计划
 *
 * @author : lijie
 * @date : 2023-5-29
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolPlanController {
    private final PatrolPlanService patrolPlanService;

    /**
     * 查询计划详情
     *
     * @param id 计划id
     *
     * @return 计划详情
     */
    @GetMapping("/electron/plan/{id}")
    public PatrolPlanVO getPatrolPlan(@PathVariable Long id) {
        return patrolPlanService.getPatrolPlan(id);
    }

    /**
     * 查询计划列表
     *
     * @param param 查询条件
     *
     * @return 计划信息分页
     */
    @GetMapping("/electron/getPalnPage")
    @DataPermissionSwitch(dataModel = "patrol:patrolPlan")
    public Pagination<PatrolPlanPageVO> getPalnPage(PatrolPlanPageParam param) {
        return patrolPlanService.getPalnPage(param);
    }

    /**
     * 查询状态计划数量
     *
     * @return 计划状态数量
     */
    @GetMapping("/electron/getPalnStatus")
    @DataPermissionSwitch(dataModel = "patrol:patrolPlan")
    public List<KeyValueVO> getPalnStatus() {
        return patrolPlanService.getPalnStatus();
    }

    /**
     * 查询用户是否有考勤应用
     *
     * @return 是否有考勤应用
     */
    @GetMapping("/electron/plan/attendance")
    public Boolean attendance() {
        return patrolPlanService.attendance();
    }

    /**
     * 停止、启动计划
     *
     * @param param 计划信息
     *
     * @return 新增结果
     */
    @PutMapping("/electron/plan")
    public Boolean update(@RequestBody PatrolPlanStatusParam param) {
        return patrolPlanService.update(param);
    }

    /**
     * 删除计划
     *
     * @param id 计划id
     *
     * @return 删除结果
     */
    @DeleteMapping("/electron/plan/delete/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return patrolPlanService.deleteById(id);
    }

    /**
     * 新增巡更计划v1.2
     *
     * @param param 计划信息
     *
     * @return id
     */
    @PostMapping("/electron/plan/add")
    public Long addPlan(@RequestBody PatrolPlanParam param) {
        return patrolPlanService.addPlan(param);
    }

    /**
     * 编辑巡更计划
     *
     * @param param 计划信息
     *
     * @return id
     */
    @PutMapping("/electron/plan/update")
    public Boolean updatePlan(@RequestBody PatrolPlanParam param) {
        return patrolPlanService.updatePlan(param);
    }

}
