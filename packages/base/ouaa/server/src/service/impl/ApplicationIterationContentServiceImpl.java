package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationIterationContentMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationIterationContent;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationContentInfo;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationIterationContentService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 09:14
 * @Description:
 */
@Service
public class ApplicationIterationContentServiceImpl extends ServiceImpl<ApplicationIterationContentMapper, ApplicationIterationContent> implements ApplicationIterationContentService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(Long id, List<ApplicationIterationContentInfo> contentList) {
        List<ApplicationIterationContent> iterationContents = this.lambdaQuery()
                .select(ApplicationIterationContent::getId)
                .eq(ApplicationIterationContent::getIterationId, id)
                .list();
        if (!CollectionUtils.isEmpty(iterationContents)) {
            this.removeByIds(iterationContents.stream().map(ApplicationIterationContent::getId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }
        List<ApplicationIterationContent> applicationIterationContents = contentList.stream().map(content -> {
            ApplicationIterationContent applicationIterationContent = ObjectUtils.copying(content, ApplicationIterationContent.class);
            applicationIterationContent.setIterationId(id);
            return applicationIterationContent;
        }).collect(Collectors.toList());
        this.saveBatch(applicationIterationContents);
    }

    @Override
    public List<ApplicationIterationContentInfo> getContentByIterationIdList(Set<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(this.lambdaQuery()
                                           .select(ApplicationIterationContent::getIterationId, ApplicationIterationContent::getCategory, ApplicationIterationContent::getContent)
                                           .in(ApplicationIterationContent::getIterationId, idList)
                                           .list()).orElse(new ArrayList<>())
                .stream()
                .map(content -> ObjectUtils.copying(content, ApplicationIterationContentInfo.class))
                .collect(Collectors.toList());
    }
}
