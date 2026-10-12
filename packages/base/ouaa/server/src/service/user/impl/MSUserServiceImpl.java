package com.guhecloud.cube.biz.ouaa.server.service.user.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.DictionaryEnum;
import com.guhecloud.cube.biz.ouaa.server.dto.login.MSTokenDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.login.MSUserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserManagedOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.UserManagedOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.user.MSUserService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  14:15
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MSUserServiceImpl implements MSUserService {
    private final SysDictionaryAdapter dictionaryAdapter;
    private final UserService userService;
    private final UserOrganizationService userOrganizationService;
    private final UserManagedOrganizationService userManagedOrganizationService;

    @Override
    public void user() {
        log.info("开始同步环天智慧园区数字大脑人员");
        List<User> updateUserList = new ArrayList<>();
        try {
            SysDictionaryDTO tockenDictionary = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.OAUTH_TOCKEN.name());
            String tockenResult = HttpUtil.get(tockenDictionary.getVariable());
            MSTokenDTO tokenDTO = JsonUtils.deserialize(tockenResult, MSTokenDTO.class);
            SysDictionaryDTO dictionaryDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.USER_GET_ALL.name());
            if (ObjectUtils.isNotEmpty(dictionaryDTO)) {
                String result = HttpUtil.get(
                        dictionaryDTO.getVariable() + "?token=" + tokenDTO.getToken() + "&usercode=" + tokenDTO.getUsercode());
                Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
                List<MSUserDTO> userDTO = JsonUtils.deserialize(map.get("data"), new TypeReference<List<MSUserDTO>>() {
                });
                userDTO.forEach(ex -> {
                    User user = ex.getUser();
                    if (ObjectUtils.isEmpty(user)) {
                        return;
                    }
                    UserModifyParam param = new UserModifyParam();
                    try {
                        List<Long> managedOrganizationIds = new ArrayList<>();
                        if (ObjectUtils.isNotEmpty(ex.getSsbm())) {
                            String[] orIds = ex.getSsbm().split(",");
                            for (String orId : orIds) {
                                managedOrganizationIds.add(Long.parseLong(orId));
                            }
                            param.setOrganizationIds(managedOrganizationIds);
                        }
                        User userInfo = userService.getOne(new LambdaQueryWrapper<User>()
                                                                   .eq(User::getMobile, user.getMobile()));
                        if (ObjectUtils.isNotEmpty(userInfo)) {
                            userInfo.setName(user.getName());
                            userInfo.setGender(user.getGender());
                            userService.updateById(userInfo);
                            //删除用户与租户的组织结构信息
                            this.userOrganizationService.remove(Wrappers.<UserOrganization>lambdaQuery()
                                                                        .eq(UserOrganization::getUserId, user.getId()));
                        } else {
                            user.setId(IdWorker.getId());
                            userService.saveByTenant(user, param);
                        }
                        // 保存管理部门
                        if (ObjectUtils.isNotEmpty(param.getManagedOrganizationIds())) {
                            List<UserManagedOrganization> userManagedOrganizations = new ArrayList<>();
                            param.getManagedOrganizationIds().forEach(managedOrganizationId -> {
                                UserManagedOrganization userManagedOrganization = new UserManagedOrganization();
                                userManagedOrganization.setUserId(user.getId());
                                userManagedOrganization.setOrganizationId(managedOrganizationId);
                                userManagedOrganizations.add(userManagedOrganization);
                            });
                            userManagedOrganizationService.saveBatch(userManagedOrganizations);
                        }
                    } catch (Exception e) {
                        log.error("同步人员失败：{}", e.getMessage(), e);
                    }
                });
            }
            log.info("同步人员结束,共{}条", updateUserList.size());
        } catch (Exception e) {
            log.error("同步人员失败：{}", e.getMessage(), e);
        }
    }
}
