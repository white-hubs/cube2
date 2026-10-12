package com.guhecloud.cube.customization.xialing.server.liangcang.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureAcquireKey;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarehouseMeasureHistoryMapper extends BaseMapper<WarehouseMeasureHistory> {

    @Select("<script>"
            + "SELECT warehouse_id AS warehouseId, acquisition_date AS acquisitionDate FROM warehouse_measure_history "
            + "WHERE (deleted IS NULL OR deleted = 0) "
            + "AND <foreach collection='keys' item='k' open='(' separator=' OR ' close=')'>"
            + "(warehouse_id = #{k.warehouseId} AND acquisition_date = #{k.acquisitionDate})"
            + "</foreach>"
            + "</script>")
    List<WarehouseMeasureAcquireKey> selectExistingKeys(@Param("keys") List<WarehouseMeasureAcquireKey> keys);

    @Select("<script>"
            + "SELECT h.* FROM warehouse_measure_history h "
            + "JOIN ("
            + "  SELECT warehouse_id, MAX(acquisition_date) AS acquisition_date "
            + "  FROM warehouse_measure_history "
            + "  WHERE (deleted IS NULL OR deleted = 0) "
            + "  AND warehouse_id IN "
            + "  <foreach collection='warehouseIds' item='warehouseId' open='(' separator=',' close=')'>"
            + "    #{warehouseId}"
            + "  </foreach>"
            + "  GROUP BY warehouse_id"
            + ") latest ON latest.warehouse_id = h.warehouse_id AND latest.acquisition_date = h.acquisition_date "
            + "WHERE (h.deleted IS NULL OR h.deleted = 0)"
            + "</script>")
    List<WarehouseMeasureHistory> selectLatestByWarehouseIds(@Param("warehouseIds") List<Long> warehouseIds);
}
