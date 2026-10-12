package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGpsHistory;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;

import java.util.List;
import java.util.Set;

public interface DispatchDeviceGpsHistoryService extends IService<DispatchDeviceGpsHistory> {

    /**
     * 根据设备主键集合获取最后一次gps信息
     *
     * @param deviceIds
     *
     * @return
     */
    List<DispatchDeviceGpsHistory> getLastGpsHistoryByDeviceIds(Set<String> deviceIds);

    /**
     * 历史规则分页列表
     *
     * @param param
     *
     * @return
     */
    Page<DispatchGPSVO> gpsHistory(DispatchGPSQueryModel param);
}
