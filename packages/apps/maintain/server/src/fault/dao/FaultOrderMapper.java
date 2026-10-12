package com.guhecloud.cube.srv.gen.maintain.server.fault.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultyEquipmentTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TroubleListDetailVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 9:27
 */
@Mapper
public interface FaultOrderMapper extends BaseMapper<FaultOrder> {
    /**
     * @Author wql
     * @Description 次数明细分页
     * @Date 15:33 2023/11/7
     **/
    Page<TroubleListDetailVO> getPageRepairsDetail(Page<TroubleListDetailVO> convert, @Param("param") TaskDetailParam param);

    /**
     * @Author wql
     * @Description 报修次数明细不分页
     * @Date 15:33 2023/11/7
     **/
    List<TroubleListDetailVO> getRepairsDetail(@Param("param") TaskDetailParam param);

    /**
     * @Author wql
     * @Description 可视化故障单统计
     * @Date 11:10 2023/11/28
     **/
    List<FaultyEquipmentTopVO> getFaultyEquipmentTop(@Param("period") Period period);

    /**
     * 设备故障数量统计
     *
     * @param param 设备id
     *
     * @return 统计结果
     */
    List<DeviceOperationStatisticsDTO> deviceFaultNumStatistics(@Param("param") DeviceStatisticParam param);

}
