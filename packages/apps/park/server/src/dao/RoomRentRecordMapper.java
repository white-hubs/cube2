package com.guhecloud.cube.srv.gen.park.management.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomRateVo;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 房间租赁记录 Mapper 接口
 * </p>
 *
 * @author zwb
 */
@Mapper
public interface RoomRentRecordMapper extends BaseMapper<RoomRentRecordPO> {

    BigDecimal getFlooRageSum();

    BigDecimal getRoomFlooRageSum();

    List<RoomRateVo> getRoomRateList();

    List<Long> getIds(@Param("id") Long id);

}
