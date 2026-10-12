package com.guhecloud.cube.deliver.project.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.DeliverPeriod;
import com.guhecloud.cube.deliver.project.merchant.dto.KeyValueDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.OperatingPriceDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpaceMapper extends BaseMapper<Space> {
    List<KeyValueDTO> getQuestionnaire(@Param("period") Period period);
    List<KeyValueDTO> getContractCountd(@Param("period") Period period);
    List<OperatingPriceDTO> getOperatingPrice();
    List<KeyValueDTO> getOperatingProportion(@Param("type") String type);
    List<KeyValueDTO> safeProductionByProportion();

}
