package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailExtendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：出入库记录明细 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableStockDetailMapper extends BaseMapper<ConsumableStockDetail> {

    Page<ConsumableStockDetailExtendVO> getStockRecordDetail(Page page, @Param("param") ConsumableStockDetailQueryParam param);

    List<ConsumableStockDetailExtendVO> getStockRecordDetail(@Param("param") ConsumableStockDetailQueryParam param);
}