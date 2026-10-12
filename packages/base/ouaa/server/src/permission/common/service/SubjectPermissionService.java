package com.guhecloud.cube.biz.ouaa.server.permission.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionConfigModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.SubjectPermissionVO;
import com.guhecloud.platform.security.permission.model.Permission;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

public interface SubjectPermissionService extends IService<SubjectPermissionPO> {

    void save(SubjectPermissionModify subjectPermissionModify);

    void saveExtendConfig(String subjectId, SubjectTypeEnum subjectType,
                          Set<SubjectPermissionConfigModify> subjectPermissionConfigModifies
                         );

    void remove(String subjectId, SubjectTypeEnum subjectType);

    void remove(String subjectId, SubjectTypeEnum subjectType, String permissionCode);

    List<SubjectPermissionVO> getSubjectPermissions(Set<String> subjectIds, SubjectTypeEnum subjectType);

    List<SubjectPermissionPO> getSubjectPermissions(String subjectId, SubjectTypeEnum subjectType);

    Set<Permission> getPermissionsBySubjectIds(Set<String> subjectIds, SubjectTypeEnum subjectType);

    List<SubjectPermissionPO> getSubjectPermissions(String subjectId, SubjectTypeEnum subjectType, String permissionCode);

    List<SubjectPermissionPO> getBindRolesPermission(@NonNull List<Long> roleIds, @NonNull List<String> codes);
}