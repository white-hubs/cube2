package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDevice;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceDetailVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度设备 Repository接口
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Repository
public interface DispatchDeviceMapper extends BaseMapper<DispatchDevice> {

    void insertOrUpdateDeviceBatch(List<DispatchDevice> datas);

    Page<DispatchDeviceDetailVO> deviceDetail(Page page, @Param("param") DispatchDeviceQueryParam param);

    List<DispatchDeviceDetailVO> deviceDetail(@Param("param") DispatchDeviceQueryParam param);

    List<DispatchDeviceDetailVO> deviceInGroupWithStatus(@Param("groupId") String groupId);

    List<DispatchDeviceVO> deviceInGroup(@Param("groupId") String groupId);

    List<DispatchGroupVO> deviceBelongGroup(@Param("deviceId") String deviceId);
}