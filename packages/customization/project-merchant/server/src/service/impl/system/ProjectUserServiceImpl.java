package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.UserDeliverDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectUserService;
import com.guhecloud.cube.deliver.project.merchant.entity.ProjectUser;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ProjectMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ProjectUserMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUser> implements ProjectUserService {

    private final ProjectMapper projectMapper;
    private final ProjectUserMapper projectUserMapper;

    @Override
    public void addUser(String projectId, UserDeliverDTO userDTO, String userId) {
        if (StringUtils.isNotEmpty(userId)) { // 邀请用户时需校验邀请者的身份
            ProjectUser projectLoginUser = this.checkedUserInProject(projectId, userId);
            if (projectLoginUser == null) {
                throw new ServiceException("请您先加入项目");
            }
            if (!projectLoginUser.getManager()) {
                throw new ServiceException("非管理员不能添加用户");
            }
            ProjectUser projectUser = this.checkedUserInProject(projectId, userDTO.getId());
            if (projectUser != null) {
                throw new ServiceException("该用户已经加入该项目");
            }
        }
        userDTO.setProjectId(projectId);
        this.buildProjectUser(projectId, userDTO);
        //        this.buildUserDept(userDTO);
        //        this.buildUserPost(userDTO);
        this.buildUserRoles(projectId, userDTO);
        //        this.buildUserDeptAuth(projectId, userDTO.getId(), userDTO.getAuthDeptIds());
    }

    private void buildUserRoles(String projectId, UserDeliverDTO userDTO) {
        //        if (userDTO.getRoleIds() == null) {
        //            return;
        //        }
        // 删除用户原有的角色
        //        userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getProjectId, projectId).eq(UserRole::getUserId, userDTO.getId()));
        //        List<UserRole> userRoles = userRoleService.lambdaQuery().eq(UserRole::getProjectId, projectId).eq(UserRole::getUserId, userDTO.getId()).list();
        //        userRoles.stream().forEach(userRole -> {
        //            // 删除用户原有的角色
        //            userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userDTO.getId()).eq(UserRole::getRoleId, userRole.getRoleId()));
        //        });
        // 保存修改后的用户角色
        //        for (String roleId : userDTO.getRoleIds()) {
        //            userRoleService.save(new UserRole().setUserId(userDTO.getId()).setRoleId(roleId).setProjectId(projectId));
        //        }
    }

    @Override
    public void removeUser(String projectId, String userId, LoginUser loginUser) {
        Project project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new ServiceException("项目不存在");
        }
        ProjectUser projectLoginUser = this.checkedUserInProject(projectId, loginUser.getId().toString());
        if (projectLoginUser == null) {
            throw new ServiceException("请先加入项目");
        }
        if (!projectLoginUser.getManager()) {
            throw new ServiceException("非管理员不能移除其他用户");
        }
        if (!ProjectService.CODE_HQ.equals(project.getCode())) {
            ProjectUser projectUser = projectUserMapper.selectOne(Wrappers.<ProjectUser>lambdaQuery()
                                                                          .eq(ProjectUser::getProjectId, projectId)
                                                                          .eq(ProjectUser::getUserId, userId));
            if (projectUser.getManager()) {
                throw new ServiceException("不能移除管理员");
            }
        }
        // 删除与资源的关联关系
        //        resourcePermissionService.remove(Wrappers.<ResourcePermission>lambdaQuery()
        //                .eq(ResourcePermission::getObjectType, ResourcePermissionService.OBJECT_TYPE_USER)
        //                .eq(ResourcePermission::getObjectId, userId)
        //        );
        // 删除与角色的关联关系
        //        this.removeUserRole(projectId, userId);
        // 删除与部门的关联关系
        //        this.removeUserDept(projectId, userId);
        // 删除与岗位的关联关系
        //TODO 岗位多选已改
        //        this.removeUserPost(projectId, userId);
        // 如果是部门的负责人或分管领导，将负责人或分管领导置空
        //        this.removeDeptManagerOrLeader(projectId, userId);
        // 删除与项目的关联关系
        projectUserMapper.delete(Wrappers.<ProjectUser>lambdaQuery().eq(ProjectUser::getProjectId, projectId)
                                         .eq(ProjectUser::getUserId, userId));
        // 删除与部门权限的关联关系
        //        userDeptAuthService.remove(Wrappers.<UserDeptAuth>lambdaQuery().eq(UserDeptAuth::getUserId, userId).eq(UserDeptAuth::getProjectId, projectId));
    }

    private void buildProjectUser(String projectId, UserDeliverDTO userDTO) {
        if (projectUserMapper.selectOne(Wrappers.<ProjectUser>lambdaQuery().eq(ProjectUser::getProjectId, projectId)
                                                .eq(ProjectUser::getUserId, userDTO.getId())) == null) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setUserId(userDTO.getId());
            projectUser.setManager(false);
            projectUserMapper.insert(projectUser);
        }
    }

    private ProjectUser checkedUserInProject(String projectId, String userId) {
        ProjectUser projectLoginUser = projectUserMapper.selectOne(Wrappers.<ProjectUser>lambdaQuery()
                                                                           .eq(ProjectUser::getProjectId, projectId)
                                                                           .eq(ProjectUser::getUserId, userId));
        if (projectLoginUser == null) {
            return null;
        }
        return projectLoginUser;
    }

    //    private void removeUserRole(String projectId, String userId) {
    //        userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getProjectId, projectId).eq(UserRole::getUserId, userId));
    ////        List<Role> roles = roleService.lambdaQuery().eq(Role::getProjectId, projectId).list();
    ////        roles.forEach(role -> userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, role.getId()).eq(UserRole::getUserId, userId)));
    //    }

    //    private void removeUserDept(String projectId, String userId) {
    //        deptUserService.remove(Wrappers.<DeptUser>lambdaQuery().eq(DeptUser::getProjectId, projectId).eq(DeptUser::getUserId, userId));
    ////        List<Dept> depts = deptService.lambdaQuery().eq(Dept::getProjectId, projectId).list();
    ////        depts.forEach(dept -> deptUserService.remove(Wrappers.<DeptUser>lambdaQuery().eq(DeptUser::getDeptId, dept.getId()).eq(DeptUser::getUserId, userId)));
    //    }

    //    private void removeUserPost(String projectId, String userId) {
    //        postUserService.remove(Wrappers.<PostUser>lambdaQuery().eq(PostUser::getProjectId, projectId).eq(PostUser::getUserId, userId));
    ////        List<Post> posts = postService.lambdaQuery().eq(Post::getProjectId, projectId).list();
    ////        posts.forEach(post -> postUserService.remove(Wrappers.<PostUser>lambdaQuery().eq(PostUser::getPostId, post.getId()).eq(PostUser::getUserId, userId)));
    //    }

    //    private void removeDeptManagerOrLeader(String projectId, String userId) {
    //        Dept deptManager = deptService.lambdaQuery().eq(Dept::getProjectId, projectId).eq(Dept::getManagerId, userId).one();
    //        if (deptManager != null) {
    //            deptManager.setManagerId("");
    //            deptService.updateById(deptManager);
    //        }
    //        List<Dept> deptLeaders = deptService.lambdaQuery().eq(Dept::getProjectId, projectId).eq(Dept::getLeaderId, userId).list();
    //        for (Dept dept : deptLeaders) {
    //            dept.setLeaderId("");
    //            deptService.updateById(dept);
    //        }
    //    }

}
