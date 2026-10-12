package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectPlanStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo.InspectPlanStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InspectPlanStatisticsMapper {

    /**
     * 查询计划统计分页。
     *
     * @param page  分页参数
     * @param param 查询条件
     *
     * @return 计划统计分页
     */
    Page<InspectPlanStatisticsVO> selectPlanStatisticsPage(Page<?> page, @Param("param") InspectPlanStatisticsParam param);
}
