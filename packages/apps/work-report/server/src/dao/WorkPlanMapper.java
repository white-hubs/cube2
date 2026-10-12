package com.guhecloud.cube.srv.gen.reportmanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlan;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：工作安排 Repository接口
 *
 * @author Appet
 * @date 2024-01-04 10:08:36
 */
@Repository
public interface WorkPlanMapper extends BaseMapper<WorkPlan> {

    List<WorkPlanStatVO> getWorkPlanStatByPerson(@Param("param") WorkPlanQueryParam param);

    List<WorkPlanStatVO> getWorkPlanStatByDept(@Param("param") WorkPlanQueryParam param);
}