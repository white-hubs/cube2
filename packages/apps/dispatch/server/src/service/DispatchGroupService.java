package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroup;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupTreeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：调度设备-分组服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
public interface DispatchGroupService extends IService<DispatchGroup> {

    String add(DispatchGroupParam param);

    void update(String id, DispatchGroupParam param);

    void delete(String id);

    void deleteByCode(String code);

    void batchDelete(List<String> ids);

    DispatchGroupVO getById(String id);

    Pagination<DispatchGroupVO> getPage(DispatchGroupQueryParam param);

    List<DispatchGroupVO> getList(DispatchGroupQueryParam param);

    List<DispatchGroupTreeVO> getListWithDevice(DispatchGroupQueryParam param);

    List<DispatchGroupTreeVO> getListWithDeviceDetail(DispatchGroupQueryParam param);

    /**
     * 移除人员
     *
     * @param code
     * @param deviceCodes
     */
    void removeMembersFromGroup(String code, List<String> deviceCodes);

    /**
     * 添加人员
     *
     * @param code
     * @param deviceCodes
     */
    void addMembersToGroup(String code, List<String> deviceCodes);

    void addAndDeleteMembersToGroup(String code, List<String> deviceCodes);

    /**
     * 同步第三方平台的组数据
     */
    void obtainThirdPlatformDevice(String code);

}