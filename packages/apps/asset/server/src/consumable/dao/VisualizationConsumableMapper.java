package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.guhecloud.cube.property.management.client.common.vo.ConsumableStockVO;
import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotStrVO;
import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotVO;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisualizationConsumableMapper {
    List<PieChartValueVO> getReduceRecord(@Param("date") DateTimeSlotStrVO date, @Param("dimension") String dimension);

    String getReduceRecordToday(@Param("date") DateTimeSlotVO date);

    List<PieChartValueVO> consumableByDep(@Param("date") DateTimeSlotStrVO date);

    List<ConsumableStockVO> stockAvailability();

}
