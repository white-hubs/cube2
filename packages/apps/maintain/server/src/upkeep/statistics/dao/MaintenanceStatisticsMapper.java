package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TrendDecimalStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintainTaskTotalParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintenanceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintainTaskNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintenanceDeviceStatisticsPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaintenanceStatisticsMapper {

    Page<MaintenanceDeviceStatisticsPageVO> getDeviceStatisticsPage(Page page, @Param("param") MaintenanceDeviceQueryParam param);

    List<MaintenanceDeviceStatisticsPageVO> getDeviceStatistics(@Param("param") MaintenanceDeviceQueryParam param);

    Integer selectMaintainTaskNumRatio(@Param("status") String status);

    List<MaintainTaskNumVO> selectMaintainTaskPersonNumRatio(@Param("status") String status);

    List<MaintainTaskNumVO> selectBeMaintainedTaskPerson();

    List<MaintainTaskNumVO> selectMaintainTaskDeviceNumRatio(@Param("status") String status);

    List<TrendDecimalStatisticsVO> selectMaintainTaskExecuteTrend(@Param("param") StatisticsQueryParam param,
                                                                  @Param("status") String status
                                                                 );

    List<TrendDecimalStatisticsVO> selectMaintainTaskTimeNum();

    Long maintainTaskTotalStat(@Param("param") MaintainTaskTotalParam param);

}
