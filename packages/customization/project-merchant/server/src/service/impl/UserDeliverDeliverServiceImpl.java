package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.PasswordUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.PinYinUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.UserDeliverDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.UserPasswordModifyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.UserQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.UserRegisterDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.ProjectUser;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.enums.UserTypeEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.UserDeliverMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ProjectMapper;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectUserService;
import com.guhecloud.cube.deliver.project.merchant.service.system.UserLinkService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.AESUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDeliverDeliverServiceImpl extends ServiceImpl<UserDeliverMapper, UserDeliver> implements UserDeliverService {

    public static final String BUSINESS_TYPE = "user";

    private final PasswordEncoder passwordEncoder;

    private final UserDeliverMapper userDeliverMapper;
    @Override
    public void save(UserDeliverDTO userDTO) {
        this.verifyUserInfo(userDTO);
        UserDeliver user = Convert.convert(UserDeliver.class,userDTO);
        // 默认密码
        user.setPassword(passwordEncoder.encode(PasswordUtils.getRandomPassword(12)));
        user.setPinyin(PinYinUtils.getHead(user.getNickname()));
        userDeliverMapper.insert(user);
        // 新增用户绑定角色信息
//        insertUserRole(userDTO.getProjectId(), user.getId(), userDTO.getRoleIds());
    }

    @Override
    public String saveCompanyUser(UserDeliverDTO userDTO) {
        this.verifyUserInfo(userDTO);
        UserDeliver user = userDTO.convert();
        // 默认密码
        user.setPassword(passwordEncoder.encode("1234321"));
        user.setPinyin(PinYinUtils.getHead(user.getNickname()));
        userDeliverMapper.insert(user);
        return user.getId();
    }



    private void verifyUserInfo(UserDeliverDTO userDTO){
        if ("admin".equalsIgnoreCase(userDTO.getUsername())) {
            throw new ServiceException("账号不能为admin");
        }
        if (userDeliverMapper.selectOne(Wrappers.<UserDeliver>lambdaQuery().eq(UserDeliver::getUsername, userDTO.getUsername())) != null) {
            throw new ServiceException("用户名已经存在");
        }
        if (StringUtils.isNotEmpty(userDTO.getMobile()) && userDeliverMapper.selectOne(Wrappers.<UserDeliver>lambdaQuery().eq(UserDeliver::getMobile, userDTO.getMobile())) != null) {
            throw new ServiceException("手机号已经存在");
        }
        if (StringUtils.isNotEmpty(userDTO.getIdentityCard()) && userDeliverMapper.selectOne(Wrappers.<UserDeliver>lambdaQuery().eq(UserDeliver::getIdentityCard, userDTO.getIdentityCard())) != null) {
            throw new ServiceException("身份证号已经存在");
        }
        if (StringUtils.isNotEmpty(userDTO.getEmail()) && userDeliverMapper.selectOne(Wrappers.<UserDeliver>lambdaQuery().eq(UserDeliver::getEmail, userDTO.getEmail())) != null) {
            throw new ServiceException("邮箱已经存在");
        }
    }
}
