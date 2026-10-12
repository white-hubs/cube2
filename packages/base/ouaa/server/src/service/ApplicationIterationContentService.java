package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationIterationContent;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationContentInfo;

import java.util.List;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 09:09
 * @Description:
 */
public interface ApplicationIterationContentService extends IService<ApplicationIterationContent> {

    /**
     * 批量保存或更新迭代内容
     *
     * @param id
     * @param contentList
     */
    void saveOrUpdateBatch(Long id, List<ApplicationIterationContentInfo> contentList);

    /**
     * 根据迭代id列表获取迭代内容
     *
     * @param idList
     *
     * @return
     */
    List<ApplicationIterationContentInfo> getContentByIterationIdList(Set<Long> idList);
}
