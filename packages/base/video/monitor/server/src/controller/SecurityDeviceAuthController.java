package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraInsideSecurityDeviceParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraUserAuthDeviceQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.SecurityDeviceAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.DeviceBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.UserBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.service.SecurityDeviceAuthService;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityDeviceAuthVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityUserAuthVO;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 设备授权
 *
 * @author lijie
 * @date 2023/3/13
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityDeviceAuthController {
    private final SecurityDeviceAuthService securityDeviceAuthService;

    private final UserClient userClient;

    /**
     * 监控权限设备列表
     *
     * @return 设备权限信息
     */
    @GetMapping("/security/device/auth/getPage")
    public Pagination<SecurityDeviceAuthVO> getSecurityDeviceAuthPage(@Valid CameraInsideSecurityDeviceParam param) {
        InsideSecurityDeviceParam query = new InsideSecurityDeviceParam();
        if (!CollectionUtils.isEmpty(param.getAreaId())) {
            String join = String.join("-", param.getAreaId());
            query.setAreaId(join);
        }
        if (Objects.equals(Boolean.TRUE, param.getAssociated())) {
            List<Long> securityDeviceIds = getRefDeviceIds(param.getUserId());
            if (CollectionUtils.isEmpty(securityDeviceIds)) {
                return Pagination.empty();
            }
            query.setDeviceIds(securityDeviceIds);
        } else if (Objects.equals(Boolean.FALSE, param.getAssociated())) {
            List<Long> securityDeviceIds = getRefDeviceIds(param.getUserId());
            query.setFilterDeviceIds(securityDeviceIds);
        }
        query.setKeyword(param.getKeyword());
        query.setStatus(param.getStatus());
        query.setGenre(param.getGenre());
        query.setApplicationCode(param.getApplicationCode());
        query.setPageNumber(param.getPageNumber());
        query.setPageSize(param.getPageSize());
        return securityDeviceAuthService.getSecurityDeviceAuthPage(query);
    }

    private List<Long> getRefDeviceIds(Long userId) {
        if (Objects.nonNull(userId)) {
            return this.securityDeviceAuthService.getSecurityDeviceIds(Collections.singletonList(userId));
        }
        List<Long> userIds = this.userClient.postList(new UserQuery()).stream().map(UserDTO::getId).collect(Collectors.toList());
        return this.securityDeviceAuthService.getSecurityDeviceIds(userIds);
    }

    /**
     * 用户监控设备列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/security/user/device/auth/getPage")
    public Pagination<SecurityUserAuthVO> getSecurityUserDeviceAuthPage(@Valid CameraUserAuthDeviceQueryParam param) {
        return securityDeviceAuthService.getSecurityUserDeviceAuthPage(param);
    }

    /**
     * 新增监控权限
     *
     * @return 新增结果
     */
    @PostMapping("/security/device/auth/insert")
    public Boolean insert(@RequestBody @Valid SecurityDeviceAuthParam param) {
        if (Boolean.TRUE.equals(param.getDeviceAll())) {
            List<Long> deviceIdList = new ArrayList<>();
            CameraInsideSecurityDeviceParam deviceParam = new CameraInsideSecurityDeviceParam();
            deviceParam.setApplicationCode(param.getApplicationCode());
            deviceParam.setAreaId(param.getAreaId());
            deviceParam.setKeyword(param.getKeyword());
            deviceParam.setAssociated(param.getAssociated());
            deviceParam.setUserId(param.getUserId());
            deviceParam.setPageSize(999);
            this.loopDeviceId(deviceParam, 1, deviceIdList, 1);
            param.setSecurityDeviceId(deviceIdList);
        }
        Assert.isNotFound(param.getSecurityDeviceId(), "设备为空!");
        return securityDeviceAuthService.insert(param);
    }

    private void loopDeviceId(CameraInsideSecurityDeviceParam deviceParam, Integer pageNumber, List<Long> deviceIdList, int index) {
        if (index > 1000) {
            return;
        }
        deviceParam.setPageNumber(pageNumber);
        Pagination<SecurityDeviceAuthVO> pagination = this.getSecurityDeviceAuthPage(deviceParam);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            pagination.getRecords().forEach(vo -> deviceIdList.add(vo.getDeviceId()));
        }
        if (pagination.getPages() > pageNumber) {
            this.loopDeviceId(deviceParam, ++pageNumber, deviceIdList, ++index);
        }

    }

    /**
     * 移除用户监控权限
     *
     * @return 移除结果
     */
    @DeleteMapping("/security/device/auth/delete")
    public Boolean delete(@Valid @NotNull(message = "设备id不能为空") String securityDeviceId,
                          @NotNull(message = "用户id不能为空") String userId
                         ) {
        return securityDeviceAuthService.delete(securityDeviceId, userId);
    }

    /**
     * 批量移除用户监控权限
     *
     * @param param
     */
    @DeleteMapping("/security/auth/batch/delete/users")
    public void batchDeleteUser(@RequestBody @Valid UserBatchDeleteParam param) {
        this.securityDeviceAuthService.batchDeleteUser(param);
    }

    /**
     * 批量移除设备监控权限
     *
     * @param param
     */
    @DeleteMapping("/security/auth/batch/delete/devices")
    public void batchDeleteDevice(@RequestBody @Valid DeviceBatchDeleteParam param) {
        this.securityDeviceAuthService.batchDeleteDevice(param);
    }
}
