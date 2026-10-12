package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceStatus;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：调度设备-状态服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
public interface DispatchDeviceStatusService extends IService<DispatchDeviceStatus> {

    String add(DispatchDeviceStatusParam param);

    void update(String id, DispatchDeviceStatusParam param);

    void delete(String id);

    void batchDelete(List<String> ids);

    DispatchDeviceStatusVO getById(String id);

    Pagination<DispatchDeviceStatusVO> getPage(DispatchDeviceStatusQueryParam param);

    /**
     * codes可以是逗号拼接的多个code
     *
     * @param codes
     *
     * @return
     */
    List<DispatchDeviceStatusVO> getListByCode(String codes, Boolean online);
}