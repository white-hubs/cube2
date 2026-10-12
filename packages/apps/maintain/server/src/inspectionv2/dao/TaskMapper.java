package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<InspectTaskPO> {

    @Select("select distinct tenant from inspect_task")
    List<Long> getTaskTenantList();

    /**
     * 获取总部任务采集数据
     *
     * @param period 采集时间段
     *
     * @return 任务集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InspectTaskPO> getTaskCollectData(@Param("tenant") Integer tenant, @Param("period") Period period);

    List<HandlerEfficiencyDTO> selectInspectTaskCompleteEfficiencyMetric(@Param("period") Period period, @Param("sort") String sort);

    TaskHandlerTimeVO selectInspectTaskCompleteTimeNumMetric(@Param("period") Period period);

    /**
     * 获取任务总数
     *
     * @param taskIds 任务ID集合（可选）
     * @param param   查询参数
     *
     * @return 任务总数
     */
    Long getTaskTotal(@Param("taskIds") List<Long> taskIds, @Param("param") InspectionCommonParam param);

}
