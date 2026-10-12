package com.guhecloud.cube.biz.ouaa.server.verifier;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.ouaa.server.dao.UserMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserTenantVerifier {
    private final UserMapper userMapper;

    public void idCardExistVerify(String idCard) {
        //校验身份证号唯一
        if (StringUtils.isNotBlank(idCard)) {
            Assert.isConflict(Objects.nonNull(userMapper.getUserByIdCard(idCard, SessionContextHolder.getRequester()
                    .getTenant())), "身份证号已存在");
        }
    }

    public void mobileExistVerify(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
            Assert.isConflict(user != null, "手机号已存在");
        }
    }
}
