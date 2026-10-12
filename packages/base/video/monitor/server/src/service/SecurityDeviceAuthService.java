package com.guhecloud.cube.biz.vmonitor.server.service;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraUserAuthDeviceQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.SecurityDeviceAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.DeviceBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.UserBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityDeviceAuthVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityUserAuthVO;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface SecurityDeviceAuthService {
    /**
     * 查询用户所有权限设备
     *
     * @return 设备id
     */
    List<Long> getSecurityDeviceAuthByUserId(String userId);

    /**
     * 查询关联的所有权限设备
     *
     * @return
     */
    List<Long> getSecurityDeviceIds(List<Long> userIds);

    /**
     * 监控权限设备列表
     *
     * @return 设备权限信息
     */
    Pagination<SecurityDeviceAuthVO> getSecurityDeviceAuthPage(InsideSecurityDeviceParam param);

    /**
     * 用户监控设备列表
     *
     * @param param
     *
     * @return
     */
    Pagination<SecurityUserAuthVO> getSecurityUserDeviceAuthPage(CameraUserAuthDeviceQueryParam param);

    /**
     * 新增监控权限
     *
     * @return 新增结果
     */
    Boolean insert(SecurityDeviceAuthParam param);

    /**
     * 移除用户监控权限
     *
     * @return 移除结果
     */
    Boolean delete(String securityDeviceId, String userId);

    /**
     * 批量删除用户监控权限
     *
     * @param param
     */
    void batchDeleteUser(UserBatchDeleteParam param);

    /**
     * 批量删除设备监控权限
     *
     * @param param
     */
    void batchDeleteDevice(DeviceBatchDeleteParam param);
}
