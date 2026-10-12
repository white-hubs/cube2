package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.system.UserLink;
import com.guhecloud.cube.deliver.project.merchant.mapper.UserDeliverMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.UserLinkMapper;
import com.guhecloud.cube.deliver.project.merchant.service.system.UserLinkService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserLinkServiceImpl extends ServiceImpl<UserLinkMapper, UserLink> implements UserLinkService {

    private final UserLinkMapper userLinkMapper;
    private final UserDeliverMapper userMapper;

    @Override
    public void saveOrUpdate(String userId, String wxopenid, String wxunionid, Integer tenant) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        if (ObjectUtil.isNotEmpty(tenant)) {
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            userMapper.insertUserTenant(snowflake.nextId(), userId, tenant);
        }
        if (!(StringUtils.isNotEmpty(wxopenid) || StringUtils.isNotEmpty(wxunionid))) {
            return;
        }
        UserDeliver user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        UserLink saved = userLinkMapper.selectOne(Wrappers.<UserLink>lambdaQuery().eq(UserLink::getUserId, userId));
        if (saved == null) {
            UserLink userLink = new UserLink();
            userLink.setUserId(userId);
            userLink.setWxopenid(wxopenid);
            userLink.setWxunionid(wxunionid);
            userLinkMapper.insert(userLink);
        } else {
            saved.setWxopenid(wxopenid);
            saved.setWxunionid(wxunionid);
            userLinkMapper.updateById(saved);
        }
    }

}
