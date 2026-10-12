package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDevice;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceUpdateParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceDetailVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：调度设备服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
public interface DispatchDeviceService extends IService<DispatchDevice> {

    String add(DispatchDeviceParam param);

    void update(String id, DispatchDeviceUpdateParam param);

    void delete(String id);

    void batchDelete(List<String> ids);

    DispatchDeviceVO getById(String id);

    Pagination<DispatchDeviceVO> getPage(DispatchDeviceQueryParam param);

    //同步第三方平台的终端设备
    void obtainThirdPlatformDevice(String code);

    List<DispatchDeviceDetailVO> getDeviceInCircle(double lng, double lat, double radius, Boolean onlineStatus);

    Pagination<DispatchDeviceDetailVO> detailPage(DispatchDeviceQueryParam param);

    List<DispatchDeviceDetailVO> detailList(DispatchDeviceQueryParam param);

    List<DispatchDeviceDetailVO> deviceInGroup(String groupId);

}