package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request.CameraDeviceQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.response.DeviceMonitoringResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.request.CameraUserQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.response.CameraUserPageResponse;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.service.CameraUserQueryService;
import com.guhecloud.cube.biz.vmonitor.server.dao.SecurityDeviceAuthMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.SecurityDeviceAuth;
import com.guhecloud.cube.biz.vmonitor.server.param.AuthUserParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraUserAuthDeviceQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.SecurityDeviceAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.DeviceBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.UserBatchDeleteParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.cube.biz.vmonitor.server.service.SecurityDeviceAuthService;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityDeviceAuthVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.SecurityUserAuthVO;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityDeviceAuthServiceImpl extends ServiceImpl<SecurityDeviceAuthMapper, SecurityDeviceAuth> implements SecurityDeviceAuthService {
    private final SecurityDeviceClient securityDeviceClient;

    private final MultipleVideoCollectCameraService multipleVideoCollectCameraService;

    private final UserClient userClient;

    private final CameraUserQueryService cameraUserQueryService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    @Override
    public List<Long> getSecurityDeviceAuthByUserId(String userId) {
        return baseMapper.getSecurityDeviceAuthByUserId(Long.parseLong(userId));
    }

    @Override
    public List<Long> getSecurityDeviceIds(List<Long> userIds) {
        return baseMapper.getSecurityDeviceIds(userIds);
    }

    @Override
    public Pagination<SecurityDeviceAuthVO> getSecurityDeviceAuthPage(InsideSecurityDeviceParam param) {
        Pagination<SecurityDeviceVo> pagination = securityDeviceClient.getSecurityDevicePage(param);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        }
        List<Long> deviceIds = pagination.getRecords().stream().map(SecurityDeviceVo::getDeviceId).collect(Collectors.toList());
        Map<Long, List<SecurityDeviceAuth>> securityDeviceAuthGroupVOS = this.baseMapper.getUserIdsByDeviceIds(deviceIds).stream()
                .collect(Collectors.groupingBy(SecurityDeviceAuth::getSecurityDeviceId));
        List<SecurityDeviceAuthVO> auth = new ArrayList<>();
        UserQuery userQuery = new UserQuery();
        List<UserDTO> userList = this.userClient.postList(userQuery);
        for (SecurityDeviceVo ex : pagination.getRecords()) {
            SecurityDeviceAuthVO vo = ConvertUtils.convert(SecurityDeviceAuthVO.class, ex);
            vo.setAreaName(ex.getAreaName());
            List<SecurityDeviceAuth> securityDeviceAuths = securityDeviceAuthGroupVOS.get(ex.getDeviceId());
            if (!CollectionUtils.isEmpty(securityDeviceAuths)) {
                List<Long> userIds = securityDeviceAuths.stream().map(SecurityDeviceAuth::getUserId).collect(Collectors.toList());
                vo.setUserList(userList.stream().filter(user -> userIds.contains(user.getId())).map(userDTO ->
                                                                                                            new AuthUserParam(userDTO.getId(), userDTO.getName())
                                                                                                   ).collect(Collectors.toList()));
            }
            auth.add(vo);
        }
        return new Pagination<>(pagination.getTotal(), pagination.getPages(), auth);
    }

    @Override
    public Pagination<SecurityUserAuthVO> getSecurityUserDeviceAuthPage(CameraUserAuthDeviceQueryParam param) {
        CameraUserQueryRequest request = ObjectUtils.copying(param, CameraUserQueryRequest.class);
        if (Objects.equals(Boolean.TRUE, param.getAssociated())) {
            //未找到已绑定的用户直接返回
            List<Long> securityUserIds = getRefUserIds(param.getDeviceId());
            if (CollectionUtils.isEmpty(securityUserIds)) {
                return Pagination.empty();
            }
            request.setUserIds(securityUserIds);
        } else if (Objects.equals(Boolean.TRUE, param.getAssociated())) {
            List<Long> securityUserIds = getRefUserIds(param.getDeviceId());
            request.setFilterUserIds(securityUserIds);
        }
        Pagination<CameraUserPageResponse> responsePagination = this.cameraUserQueryService.queryCameraUserPage(request);
        if (CollectionUtils.isEmpty(responsePagination.getRecords())) {
            return Pagination.empty();
        }
        List<SecurityUserAuthVO> securityUserAuthVOS = ObjectUtils.copying(responsePagination.getRecords(), SecurityUserAuthVO.class);
        List<Long> userIds = securityUserAuthVOS.stream().map(SecurityUserAuthVO::getId).collect(Collectors.toList());
        Map<Long, List<SecurityDeviceAuth>> securityDeviceAuthMap = this.baseMapper
                .selectList(Wrappers.<SecurityDeviceAuth>lambdaQuery().in(SecurityDeviceAuth::getUserId, userIds))
                .stream()
                .collect(Collectors.groupingBy(SecurityDeviceAuth::getUserId));
        if (!MapUtils.isEmpty(securityDeviceAuthMap)) {
            List<Long> deviceIds = new ArrayList<>();
            securityDeviceAuthMap.forEach((key, value) -> deviceIds
                    .addAll(value.stream().map(SecurityDeviceAuth::getSecurityDeviceId).collect(Collectors.toList())));
            CameraDeviceQueryRequest cameraDeviceQueryRequest = new CameraDeviceQueryRequest();
            cameraDeviceQueryRequest.setDeviceIds(deviceIds);
            List<DeviceMonitoringResponse> monitoringResponses = this.cameraSecurityDeviceQueryService
                    .queryCameraDeviceList(cameraDeviceQueryRequest);
            securityUserAuthVOS.stream().filter(securityUserAuthVO -> securityDeviceAuthMap.containsKey(securityUserAuthVO.getId()))
                    .forEach(securityUserAuthVO -> {
                        List<Long> userDeviceIds = securityDeviceAuthMap.get(securityUserAuthVO.getId())
                                .stream().map(SecurityDeviceAuth::getSecurityDeviceId).collect(Collectors.toList());
                        securityUserAuthVO.setDevices(monitoringResponses.stream()
                                                              .filter(deviceMonitoringVo -> userDeviceIds
                                                                      .contains(deviceMonitoringVo.getDeviceId()))
                                                              .map(deviceMonitoringVo -> new SecurityUserAuthVO.SecurityUserDevices(deviceMonitoringVo
                                                                                                                                            .getDeviceId(), deviceMonitoringVo
                                                                                                                                            .getDeviceName()))
                                                              .collect(Collectors.toList()));
                    });
        }
        return Pagination.<SecurityUserAuthVO>builder().pages(responsePagination.getPages()).total(responsePagination.getTotal())
                .records(securityUserAuthVOS).build();

    }

    private List<Long> getRefUserIds(Long deviceId) {
        if (Objects.nonNull(deviceId)) {
            return this.baseMapper.getSecurityUserIds(Collections.singletonList(deviceId));
        }
        List<Long> deviceIds = this.cameraSecurityDeviceQueryService.getDeviceIds();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return this.baseMapper.getSecurityUserIds(deviceIds);
    }

    @Override
    public Boolean insert(SecurityDeviceAuthParam param) {
        for (Long ex : param.getSecurityDeviceId()) {
            for (AuthUserParam userParam : param.getUserList()) {
                SecurityDeviceAuth auth = ConvertUtils.convert(SecurityDeviceAuth.class, userParam);
                auth.setSecurityDeviceId(ex);
                baseMapper.insert(auth);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String securityDeviceId, String userId) {
        boolean successful = baseMapper.delete(new LambdaQueryWrapper<SecurityDeviceAuth>()
                                                       .eq(SecurityDeviceAuth::getSecurityDeviceId, securityDeviceId)
                                                       .eq(SecurityDeviceAuth::getUserId, userId)) > 0;
        this.multipleVideoCollectCameraService.removeByUser(Long.valueOf(userId), Long.valueOf(securityDeviceId));
        return successful;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUser(UserBatchDeleteParam param) {
        CameraUserQueryRequest cameraUserQueryRequest = new CameraUserQueryRequest();
        cameraUserQueryRequest.setKeyword(param.getKeyword());
        cameraUserQueryRequest.setOrganizationIds(param.getOrganizationIds());
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            cameraUserQueryRequest.setUserIds(param.getUserIds().stream().collect(Collectors.toList()));
        }
        Set<Long> userIds = this.cameraUserQueryService.queryCameraUserIds(cameraUserQueryRequest);
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        baseMapper.delete(new LambdaQueryWrapper<SecurityDeviceAuth>()
                                  .in(SecurityDeviceAuth::getUserId, userIds));
        this.multipleVideoCollectCameraService.removeByUserIds(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDevice(DeviceBatchDeleteParam param) {
        CameraDeviceQueryRequest request = new CameraDeviceQueryRequest();
        request.setKeyword(param.getKeyword());
        request.setDeviceIds(param.getDeviceIds());
        request.setAreaId(param.getAreaId());
        List<DeviceMonitoringResponse> deviceMonitoringResponses = this.cameraSecurityDeviceQueryService.queryCameraDeviceList(request);
        if (CollectionUtils.isEmpty(deviceMonitoringResponses)) {
            return;
        }
        Set<Long> deviceIds = deviceMonitoringResponses.stream().map(DeviceMonitoringResponse::getDeviceId).collect(Collectors.toSet());
        baseMapper.delete(new LambdaQueryWrapper<SecurityDeviceAuth>()
                                  .in(SecurityDeviceAuth::getSecurityDeviceId, deviceIds));
        this.multipleVideoCollectCameraService.removeByDeviceIds(deviceIds);
    }
}
