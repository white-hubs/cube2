package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DisinfectionUserPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.BimBaseQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BimStatisticsDisinfectionService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimUserStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.DisinfectionUserInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsCycleVO;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 可视化面板统计接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 16:50
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/bim/statistics/disinfection")
public class BimStatisticsDisinfectionController {

    private final BimStatisticsDisinfectionService bimStatisticsDisinfectionService;

    /**
     * 消杀项目统计
     */
    @GetMapping("/project/status")
    public BimProjectStatisticsVO disinfectionProjectStatistics(BimBaseQueryParam param) {
        return bimStatisticsDisinfectionService.disinfectionProjectStatistics(param);
    }

    /**
     * 项目分布情况
     */
    @GetMapping("/project/distribution")
    public List<ProjectStatisticsBaseVO> disinfectionProjectDistribution() {
        return bimStatisticsDisinfectionService.disinfectionProjectDistribution();
    }

    /**
     * 项目周期情况
     */
    @GetMapping("/project/cycle")
    public ProjectStatisticsCycleVO disinfectionProjectCycle() {
        return bimStatisticsDisinfectionService.disinfectionProjectCycle();
    }

    /**
     * 重点场所类别数量分布
     */
    @GetMapping("/project/focus/distribution")
    public List<ProjectStatisticsBaseVO> disinfectionProjectFocusDistribution(KeyPlacesQueryParam param) {
        return bimStatisticsDisinfectionService.disinfectionProjectFocusDistribution(param);
    }

    /**
     * 人员情况分布
     */
    @GetMapping("/personnel/distribution")
    public BimUserStatisticsVO disinfectionPersonnelDistribution(DisinfectionUserPageParam param) {
        return bimStatisticsDisinfectionService.disinfectionPersonnelDistribution(param);
    }

    /**
     * 人员情况列表
     */
    @GetMapping("/personnel/distribution/list")
    public List<DisinfectionUserInfoVO> disinfectionPersonnelDistributionList(DisinfectionUserPageParam param) {
        return bimStatisticsDisinfectionService.disinfectionPersonnelDistributionList(param);
    }

    /**
     * 重点场所地址分布
     */
    @GetMapping("/focus/distribution")
    public List<ProjectStatisticsBaseVO> disinfectionFocusDistribution() {
        return bimStatisticsDisinfectionService.disinfectionFocusDistribution();
    }

    /**
     * 人员部门分布
     */
    @GetMapping("/personnel/department")
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelDepartmentDistribution() {
        return bimStatisticsDisinfectionService.disinfectionPersonnelDepartmentDistribution();
    }

    /**
     * 人员岗位分布
     */
    @GetMapping("/personnel/post")
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelPostDistribution() {
        return bimStatisticsDisinfectionService.disinfectionPersonnelPostDistribution();
    }

    /**
     * 人员任务完成统计
     */
    @GetMapping("/personnel/task/complete")
    public List<ProjectStatisticsBaseVO> disinfectionPersonnelTaskComplete(@RequestParam("period") Period period) {
        return bimStatisticsDisinfectionService.disinfectionPersonnelTaskComplete(period);
    }

}
