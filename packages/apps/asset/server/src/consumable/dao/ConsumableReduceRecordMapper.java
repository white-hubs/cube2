package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordExcelVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceStockStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：耗材出库记录 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableReduceRecordMapper extends BaseMapper<ConsumableReduceRecord> {

    List<ConsumableReduceStockStatVO> getReduceStockStat(@Param("param") ConsumableReduceRecordParam param);

    Page<ConsumableStoreStockExtendVO> getReduceDetail(Page page, @Param("param") ConsumableReduceRecordParam param);

    List<ConsumableReduceRecordExcelVO> getExcel(@Param("id") String id);

    Page<ConsumableReduceRecordVO> getPage(@Param("page") Page page, @Param("param") ConsumableReduceRecordQueryParam param);
}