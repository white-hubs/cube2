package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.FriendLinkConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.FriendLinkMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.FriendLink;
import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.FriendLinkService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.FriendLinkVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 友情链接Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink>
        implements FriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FriendLinkParam param) {
        FriendLink entity = FriendLinkConverts.toEntity(param);
        friendLinkMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, FriendLinkParam param) {
        FriendLink entity = friendLinkMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("友链不存在");
        }
        entity.setLinkName(param.getLinkName());
        entity.setLinkUrl(param.getLinkUrl());
        friendLinkMapper.updateById(entity);
    }

    @Override
    public FriendLinkVO detail(Long id) {
        FriendLink entity = friendLinkMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return FriendLinkConverts.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer status) {
        FriendLink entity = friendLinkMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("友链不存在");
        }
        entity.setStatus(status);
        friendLinkMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        friendLinkMapper.deleteById(id);
    }

    @Override
    public List<FriendLinkVO> list(Integer status) {
        LambdaQueryWrapper<FriendLink> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotNull(status)) {
            wrapper.eq(FriendLink::getStatus, status);
        }
        wrapper.orderByDesc(FriendLink::getCreateTime);
        return friendLinkMapper.selectList(wrapper).stream()
                .map(FriendLinkConverts::toVO)
                .collect(Collectors.toList());
    }

}
