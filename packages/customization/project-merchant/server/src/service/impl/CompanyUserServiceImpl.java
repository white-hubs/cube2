package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyUserMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商铺-用户关联 服务实现类
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyUserServiceImpl extends ServiceImpl<CompanyUserMapper, CompanyUser> implements CompanyUserService {

    private final CompanyUserMapper companyUserMapper;

    @Override
    public List<String> queryCompanyUsers(CompanyUserQueryDTO queryDTO) {
        List<CompanyUser> companyUsers = companyUserMapper.queryCompanyUsers(queryDTO);
        return companyUsers.stream().map(CompanyUser::getUserId).collect(Collectors.toList());
    }

    @Override
    public void save(CompanyUserDTO dto) {
        if (dto.getOwner()) {
            List<CompanyUser> companyStallUsers = companyUserMapper.selectList(Wrappers.<CompanyUser>lambdaQuery()
                                                                                       .eq(CompanyUser::getCompanyId, dto.getCompanyId())
                                                                                       .eq(CompanyUser::getStallId, dto.getStallId())
                                                                                       .eq(CompanyUser::getOwner, true));
            if (companyStallUsers != null && companyStallUsers.size() > 0) {
                throw new ServiceException("该档位已经绑定了档主");
            }
            if (ObjectUtil.isNotEmpty(dto.getCompanyId()) && ObjectUtil.isEmpty(dto.getStallId())) {
                List<CompanyUser> companyUsers = companyUserMapper.selectList(Wrappers.<CompanyUser>lambdaQuery()
                                                                                      .eq(CompanyUser::getCompanyId, dto.getCompanyId())
                                                                                      .eq(CompanyUser::getOwner, true));
                if (companyUsers != null && companyUsers.size() > 0) {
                    throw new ServiceException("该档位已经绑定了档主");
                }
            }
        }
        CompanyUser companyUser = companyUserMapper.selectOne(Wrappers.<CompanyUser>lambdaQuery()
                                                                      .eq(ObjectUtil.isNotEmpty(dto.getCompanyId()), CompanyUser::getCompanyId, dto.getCompanyId())
                                                                      .eq(ObjectUtil.isNotEmpty(dto.getStallId()), CompanyUser::getStallId, dto.getStallId())
                                                                      .eq(CompanyUser::getUserId, dto.getUserId()));
        if (companyUser == null) {
            companyUserMapper.insert(dto.convert());
        }
    }

    @Override
    public void removeUser(String companyId, String stallId, String userId, String loginUserId) {
        if (ObjectUtil.isNotEmpty(stallId)) {
            companyUserMapper.delete(Wrappers.<CompanyUser>lambdaQuery().eq(CompanyUser::getStallId, stallId)
                                             .eq(CompanyUser::getUserId, userId));
        } else if (ObjectUtil.isNotEmpty(companyId)) {
            companyUserMapper.delete(Wrappers.<CompanyUser>lambdaQuery().eq(CompanyUser::getCompanyId, companyId)
                                             .eq(CompanyUser::getUserId, userId));
        } else {
            companyUserMapper.delete(Wrappers.<CompanyUser>lambdaQuery().eq(CompanyUser::getUserId, userId));
        }
        //TODO 关联删除，包括实名认证等

    }

    //                companyUser.setStallId(spaces.get(0).getId());
    //                companyUserMapper.updateById(companyUser);
    //            }
    //        });
    //    }

}
