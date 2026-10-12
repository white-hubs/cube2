package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGroupRefer;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceGroupReferVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：调度组与设备关联服务层
 *
 * @author Appet
 * @date 2022-08-09 10:08:36
 */
public interface DispatchDeviceGroupReferService extends IService<DispatchDeviceGroupRefer> {

    void add(DispatchDeviceGroupReferParam param);

    void update(Long id, DispatchDeviceGroupReferParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    DispatchDeviceGroupReferVO getById(Long id);

    Pagination<DispatchDeviceGroupReferVO> getPage(DispatchDeviceGroupReferQueryParam param);

    void batchAdd(String groupId, List<String> members);

    void batchAdd(List<DispatchDeviceGroupRefer> refers);

    void delete(String groupId, String deviceId);

    void batchDelete(String groupId, List<String> members);

}