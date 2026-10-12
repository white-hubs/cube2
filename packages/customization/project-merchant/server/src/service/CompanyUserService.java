package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserQueryDTO;

import java.util.List;

/**
 * <p>
 * 商铺-用户关联 服务类
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
public interface CompanyUserService extends IService<CompanyUser> {

    void save(CompanyUserDTO dto);

    void removeUser(String companyId, String stallId, String userId, String loginUserId);

    List<String> queryCompanyUsers(CompanyUserQueryDTO queryDTO);
}
