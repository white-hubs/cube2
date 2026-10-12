package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;

import java.util.List;

public interface UserDeliverService extends IService<UserDeliver> {

    public static String ADMIN_USERNAME = "admin";
    public static String DEFAULT_USER_PASSWORD = "123456";

    void save(UserDeliverDTO userDto);

    /**
     * 新增USER，并且返回ID
     * @param userDto userDto
     * @return 新增USER的id
     */
    String saveCompanyUser(UserDeliverDTO userDto);

}
