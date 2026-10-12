package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotStrVO;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PropertyVisualizationMapper {
    List<PieChartValueVO> getPropertyProportionByStatus();

    List<PieChartValueVO> getPropertyProportionByDep();

    Integer getPropertyNumber(@Param("businessType") String businessType, @Param("date") DateTimeSlotStrVO date,
                              @Param("usageDate") LocalDateTime usageDate
                             );

    Integer getWeedOutNumber(@Param("date") DateTimeSlotStrVO date);

    List<PieChartValueVO> getPropertyType(@Param("date") DateTimeSlotStrVO date, @Param("typeName") List<String> typeName);

    List<StatisticalChartDataVO> getPropertyByDepHistogramValue(@Param("date") DateTimeSlotStrVO date);

    List<PieChartValueVO> getPropertyByDepPie(@Param("date") DateTimeSlotStrVO date);

    List<PieChartValueVO> getPropertyBySourcePie(@Param("date") DateTimeSlotStrVO date);

}
