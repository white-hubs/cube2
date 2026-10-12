package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddExcelVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddPageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：耗材入库 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableAddRecordMapper extends BaseMapper<ConsumableAddRecord> {

    Page<ConsumableAddPageVO> getPage(@Param("page") Page page, @Param("param") ConsumableAddRecordQueryParam param);

    List<ConsumableAddExcelVO> getExcel(@Param("id") String id);
}