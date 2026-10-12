package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGpsHistory;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DispatchDeviceGpsHistoryMapper extends BaseMapper<DispatchDeviceGpsHistory> {

    List<DispatchDeviceGpsHistory> selectLastGpsHistoryByDeviceIds(@Param("deviceIds") Set<String> deviceIds);

    Page<DispatchGPSVO> selectHistory(Page page, @Param("param") DispatchGPSQueryModel param);
}
