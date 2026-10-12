package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolPersonMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolPerson;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolAuthTypeEnum;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.CameraPatrolAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.DepartmentAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.RoleAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.UserAuthParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolPersonService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolPersonServiceImpl extends ServiceImpl<CameraPatrolPersonMapper, CameraPatrolPerson> implements CameraPatrolPersonService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull Long cameraPatrolLineId, @NonNull List<CameraPatrolAuthParam> personModifyParams) {
        if (CollectionUtils.isEmpty(personModifyParams)) {
            return;
        }
        List<CameraPatrolPerson> cameraPatrolPeople = new ArrayList<>();
        personModifyParams.stream().forEach(personModifyParam -> {
            CameraPatrolPerson cameraPatrolPerson = new CameraPatrolPerson();
            cameraPatrolPerson.setAuthType(personModifyParam.getAuthType());
            cameraPatrolPerson.setAuthConfig(JsonUtils.serialize(personModifyParam.getAuthConfig()));
            cameraPatrolPerson.setCameraPatrolLineId(cameraPatrolLineId);
            if (CameraPatrolAuthTypeEnum.DEPARTMENT == personModifyParam.getAuthType()) {
                List<DepartmentAuthParam> departmentAuthParams = JsonUtils.deserialize(personModifyParam.getAuthConfig(), new TypeReference<List<DepartmentAuthParam>>() {
                });
                cameraPatrolPerson.setAuthIds(departmentAuthParams.stream()
                                                      .map(departmentAuthParam -> departmentAuthParam.getDepartmentId().toString())
                                                      .collect(Collectors.joining(",")));
            } else if (CameraPatrolAuthTypeEnum.ROLE == personModifyParam.getAuthType()) {
                List<RoleAuthParam> roleAuthParams = JsonUtils.deserialize(personModifyParam.getAuthConfig(), new TypeReference<List<RoleAuthParam>>() {
                });
                cameraPatrolPerson.setAuthIds(roleAuthParams.stream().map(roleAuthParam -> roleAuthParam.getRoleId().toString())
                                                      .collect(Collectors.joining(",")));
            } else if (CameraPatrolAuthTypeEnum.USER == personModifyParam.getAuthType()) {
                List<UserAuthParam> userAuthParams = JsonUtils.deserialize(personModifyParam.getAuthConfig(), new TypeReference<List<UserAuthParam>>() {
                });
                cameraPatrolPerson.setAuthIds(userAuthParams.stream().map(userAuthParam -> userAuthParam.getUserId().toString())
                                                      .collect(Collectors.joining(",")));
            }
            cameraPatrolPeople.add(cameraPatrolPerson);

        });
        this.saveBatch(cameraPatrolPeople);
    }

    @Override
    public List<CameraPatrolPerson> listCameraPatrolPersons(@NonNull Long cameraPatrolLineId) {
        return this.lambdaQuery().eq(CameraPatrolPerson::getCameraPatrolLineId, cameraPatrolLineId)
                .list();
    }

    @Override
    public void removeByCameraPatrolLineId(@NonNull Long cameraPatrolLineId) {
        List<CameraPatrolPerson> cameraPatrolPeople = this.listCameraPatrolPersons(cameraPatrolLineId);
        if (!CollectionUtils.isEmpty(cameraPatrolPeople)) {
            DatabaseContextHolder.softDelete(this, cameraPatrolPeople);
        }
    }
}
