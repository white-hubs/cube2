package com.guhecloud.cube.base.flow.context.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.context.commandgateway.FormPermissionService;
import com.guhecloud.cube.base.flow.context.dao.FormPermissionMapper;
import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormPermissionVO;
import com.guhecloud.cube.base.flow.context.po.FormPermissionPO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 15:01
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormPermissionServiceImpl extends ServiceImpl<FormPermissionMapper, FormPermissionPO> implements FormPermissionService {
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long id, PermissionType permissionType, List<Long> permissionIds) {
        List<FormPermissionPO> permissionPOS = this.lambdaQuery()
                .select(FormPermissionPO::getId)
                .eq(FormPermissionPO::getFormFlowId, id)
                .list();
        if (!CollectionUtils.isEmpty(permissionPOS)) {
            this.removeByIds(permissionPOS.stream().map(FormPermissionPO::getId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(permissionIds) || PermissionType.ALL.equals(permissionType)) {
            return;
        }
        List<FormPermissionPO> permissionPOList = permissionIds.stream().map(t -> {
            FormPermissionPO formPermissionPO = new FormPermissionPO();
            formPermissionPO.setId(IdWorker.getId());
            formPermissionPO.setFormFlowId(id);
            formPermissionPO.setPermissionId(t);
            formPermissionPO.setPermissionType(permissionType);
            return formPermissionPO;
        }).collect(Collectors.toList());

        this.saveBatch(permissionPOList);
    }

    @Override
    public FormPermissionVO getPermissionByFormFlowId(Long id) {
        FormPermissionVO formPermissionVO = new FormPermissionVO();
        List<FormPermissionPO> formPermissionPOS = this.lambdaQuery()
                .eq(FormPermissionPO::getFormFlowId, id).list();
        if (CollectionUtils.isEmpty(formPermissionPOS)) {
            formPermissionVO.setPermissionType(PermissionType.ALL);
            return formPermissionVO;
        }
        PermissionType permissionType = formPermissionPOS.get(0).getPermissionType();
        List<Long> permissionIds = formPermissionPOS.stream().map(FormPermissionPO::getPermissionId)
                .distinct().collect(Collectors.toList());
        formPermissionVO.setPermissionType(permissionType);
        formPermissionVO.setPermissionList(permissionType.getPerMissions(permissionIds));
        return formPermissionVO;
    }

    @Override
    public List<Long> getCurrentUserPermissionList() {
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        if (user == null) {
            return new ArrayList<>();
        }
        Long organizationId = user.getOrganization() == null ? null : user.getOrganization().getId();
        List<Long> roles = Optional.ofNullable(user.getRoles()).orElse(new ArrayList<>())
                .stream().map(UserDTO.Role::getId).collect(Collectors.toList());
        List<Long> formFlowIds = this.baseMapper.selectCurrentUserForm(user.getId(), organizationId, roles);
        if (CollectionUtils.isEmpty(formFlowIds)) {
            return new ArrayList<>();
        }
        return formFlowIds;
    }
}
