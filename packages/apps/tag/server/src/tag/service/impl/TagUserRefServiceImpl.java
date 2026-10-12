package com.guhecloud.cube.srv.gen.tag.tag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.tag.tag.dao.TagUserRefMapper;
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagUserRef;
import com.guhecloud.cube.srv.gen.tag.tag.service.TagUserRefService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 14:08
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagUserRefServiceImpl extends ServiceImpl<TagUserRefMapper, TagUserRef> implements TagUserRefService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateUserRef(Long id, List<Long> userIdList) {
        this.remove(new LambdaQueryWrapper<TagUserRef>().eq(TagUserRef::getTagId, id));
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<TagUserRef> tagUserRefs = userIdList.stream().map(userId -> {
            TagUserRef tagUserRef = new TagUserRef();
            tagUserRef.setUserId(userId);
            tagUserRef.setTagId(id);
            tagUserRef.setId(IdWorker.getId());
            return tagUserRef;
        }).collect(Collectors.toList());
        this.saveBatch(tagUserRefs);
    }

    @Override
    public Map<Long, List<Long>> getTagRefUser(List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return new HashMap<>();
        }
        List<TagUserRef> tagUserRefs = this.lambdaQuery()
                .in(TagUserRef::getTagId, tagIds)
                .list();
        if (CollectionUtils.isEmpty(tagUserRefs)) {
            return new HashMap<>();
        }
        return tagUserRefs.stream()
                .collect(Collectors.groupingBy(TagUserRef::getTagId, Collectors.mapping(TagUserRef::getUserId, Collectors.toList())));
    }
}
