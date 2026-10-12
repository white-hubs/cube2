package com.guhecloud.cube.deliver.project.merchant.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.UserDeliverDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.ProjectUser;

public interface ProjectUserService extends IService<ProjectUser> {
    void addUser(String projectId, UserDeliverDTO userDTO, String loginUserId);

    void removeUser(String projectId, String userId, LoginUser loginUser);

}
