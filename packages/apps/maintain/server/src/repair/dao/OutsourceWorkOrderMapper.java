package com.guhecloud.cube.srv.gen.maintain.server.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceWorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 委外维修单
 * @date 2024/1/8 15:59
 */
@Mapper
public interface OutsourceWorkOrderMapper extends BaseMapper<OutsourceWorkOrder> {

    List<DeviceOperationStatisticsDTO> selectRepairNumberByDeviceId(@Param("param") DeviceStatisticParam param);
}
