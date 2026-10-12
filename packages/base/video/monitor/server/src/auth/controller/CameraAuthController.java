package com.guhecloud.cube.biz.vmonitor.server.auth.controller;

import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.ScopeTypeEnum;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthGroupModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.query.AuthQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGroupPO;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthGrantTargetService;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthGroupService;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthGrantTargetVO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthScopeVO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthVO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CameraAuthController {

    private final AuthGroupService authGroupService;

    private final AuthGrantTargetService authGrantTargetService;

    private final AuthScopeService authScopeService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    public CameraAuthController(AuthGroupService authGroupService, AuthGrantTargetService authGrantTargetService,
                                AuthScopeService authScopeService, CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService
                               ) {
        this.authGroupService = authGroupService;
        this.authGrantTargetService = authGrantTargetService;
        this.authScopeService = authScopeService;
        this.cameraSecurityDeviceQueryService = cameraSecurityDeviceQueryService;
    }

    /**
     * 监控权限新增
     *
     * @param param 权限新增参数
     *
     * @return 权限id
     */
    @PostMapping("/camera-auths")
    public Long saveCameraAuth(@RequestBody @Valid AuthGroupModifyParam param) {
        return authGroupService.increaseAuthGroup(param);
    }

    /**
     * 监控权限编辑
     *
     * @param id    权限id
     * @param param 权限编辑参数
     */
    @PutMapping("/camera-auths/{id}")
    public void updateCameraAuth(@PathVariable("id") Long id, @RequestBody @Valid AuthGroupModifyParam param) {
        authGroupService.updateAuthGroup(id, param);
    }

    /**
     * 监控权限分页列表
     *
     * @param param 查询参数
     *
     * @return 权限配置分页列表
     */
    @GetMapping("/camera-auths")
    public Pagination<AuthVO> getAuthPage(@Valid AuthQueryParam param) {
        var authGroupPOPage = this.authGroupService.queryPage(param);
        if (CollectionUtils.isEmpty(authGroupPOPage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(authGroupPOPage, extensions(authGroupPOPage.getRecords()));
    }

    /**
     * 监控权限详情
     *
     * @param id 权限id
     *
     * @return 权限配置
     */
    @GetMapping("/camera-auths/{id}")
    public AuthVO getAuth(@PathVariable("id") Long id) {
        var authGroup = this.authGroupService.getAuthGroup(id);
        Assert.isNotFound(authGroup, "权限配置不存在或被已删除");
        return extensions(Collections.singletonList(authGroup)).getFirst();
    }

    /**
     * 监控权限删除
     *
     * @param id 权限id
     */
    @DeleteMapping("/camera-auths/{id}")
    public void deleteCameraAuth(@PathVariable("id") Long id) {
        authGroupService.deleteAuthGroup(id);
    }

    private List<AuthVO> extensions(List<AuthGroupPO> authGroupPOS) {
        var authIds = authGroupPOS.stream().map(AuthGroupPO::getId).collect(Collectors.toSet());
        var authGroupScopeMap = authScopeService.getAuthScopesVOSByAuthGroupIds(authIds)
                .stream().collect(Collectors.groupingBy(AuthScopeVO::getAuthGroupId));
        var authGroupGrantTargetMap = authGrantTargetService.getAuthGrantTargetVOSByAuthGroupIds(authIds)
                .stream().collect(Collectors.groupingBy(AuthGrantTargetVO::getAuthGroupId));
        return authGroupPOS.stream().map(authGroupPO -> {
            var authScopePOS = authGroupScopeMap.get(authGroupPO.getId());
            AuthVO authVO = new AuthVO();
            authVO.setId(authGroupPO.getId());
            authVO.setGrantTargets(authGroupGrantTargetMap.get(authGroupPO.getId()));
            if (CollectionUtils.isNotEmpty(authScopePOS)) {
                var authScopeAuthMap = authScopePOS.stream()
                        .collect(Collectors.groupingBy(AuthScopeVO::getScopeType, Collectors.mapping(AuthScopeVO::getScopeId, Collectors.toSet())));
                var deviceIds = cameraSecurityDeviceQueryService.queryCameraDeviceIdsByAreasOrGroups(authScopeAuthMap.get(ScopeTypeEnum.DEVICE_AREA),
                                                                                                     authScopeAuthMap.get(ScopeTypeEnum.DEVICE_GROUP),
                                                                                                     authScopeAuthMap.get(ScopeTypeEnum.DEVICE));
                authVO.setAuthScopes(authScopePOS);
                authVO.setDeviceCount(deviceIds.size());
            }
            return authVO;
        }).collect(Collectors.toList());
    }
}
