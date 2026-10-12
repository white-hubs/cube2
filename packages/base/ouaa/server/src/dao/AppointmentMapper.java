package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/08
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    AppointmentDetailVO getById(@Param("id") Long id);

    List<AppointmentDetailVO> getByIds(@Param("ids") List<Long> ids);

}
