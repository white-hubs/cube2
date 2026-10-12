package com.guhecloud.cube.customization.xialing.server.liangcang.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseGoodsAcquireKey;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarehouseGoodsHistoryMapper extends BaseMapper<WarehouseGoodsHistory> {

    @Select("<script>"
            + "SELECT warehouse_id AS warehouseId, acquisition_date AS acquisitionDate FROM warehouse_goods_history "
            + "WHERE (deleted IS NULL OR deleted = 0) "
            + "AND <foreach collection='keys' item='k' open='(' separator=' OR ' close=')'>"
            + "(warehouse_id = #{k.warehouseId} AND acquisition_date = #{k.acquisitionDate})"
            + "</foreach>"
            + "</script>")
    List<WarehouseGoodsAcquireKey> selectExistingKeys(@Param("keys") List<WarehouseGoodsAcquireKey> keys);
}
