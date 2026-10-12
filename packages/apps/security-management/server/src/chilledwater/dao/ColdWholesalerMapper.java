package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWholesaler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ColdWholesalerMapper extends BaseMapper<ColdWholesaler> {
    Page<WholesalerVo> getWholesalers(Page page, @Param("keyword") String keyword);

    List<DataDeviceVo> getWholesalerDevices();

    List<DataDeviceVo> getWholesalerDevicesForUpdate(@Param("deviceCode") String deviceCode);

}
