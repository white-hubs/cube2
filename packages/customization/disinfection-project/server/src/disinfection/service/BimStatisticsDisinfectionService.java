package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DisinfectionUserPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.BimBaseQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.BimUserStatisticsVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.DisinfectionUserInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsCycleVO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 16:53
 * @Description:
 */
public interface BimStatisticsDisinfectionService {

    /**
     * 消毒项目统计
     *
     * @return
     */
    BimProjectStatisticsVO disinfectionProjectStatistics(BimBaseQueryParam param);

    /**
     * 消毒项目分布
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> disinfectionProjectDistribution();

    /**
     * 消毒项目周期
     *
     * @return
     */
    ProjectStatisticsCycleVO disinfectionProjectCycle();

    /**
     * 重点场所数量分布
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> disinfectionProjectFocusDistribution(KeyPlacesQueryParam param);

    /**
     * 人员分布情况
     *
     * @return
     */
    BimUserStatisticsVO disinfectionPersonnelDistribution(DisinfectionUserPageParam param);

    /**
     * 人员分布情况列表
     *
     * @param param
     *
     * @return
     */
    List<DisinfectionUserInfoVO> disinfectionPersonnelDistributionList(DisinfectionUserPageParam param);

    /**
     * 人员部门分布
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> disinfectionPersonnelDepartmentDistribution();

    /**
     * 人员岗位分布
     *
     * @return
     */

    List<ProjectStatisticsBaseVO> disinfectionPersonnelPostDistribution();

    /**
     * 人员任务完成情况
     *
     * @param period
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> disinfectionPersonnelTaskComplete(Period period);

    /**
     * 重点场所所在区分布
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> disinfectionFocusDistribution();

}
