package com.guhecloud.cube.srv.gen.tag.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagUserRef;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 14:08
 * @Description:
 */
public interface TagUserRefService extends IService<TagUserRef> {
    void saveOrUpdateUserRef(Long id, List<Long> userIdList);

    Map<Long, List<Long>> getTagRefUser(List<Long> tagIds);
}
