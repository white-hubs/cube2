package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationIterationRecordMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationIterationRecord;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationContentInfo;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationIterationContentService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationIterationRecordService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationIterationRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 17:53
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationIterationRecordServiceImpl extends ServiceImpl<ApplicationIterationRecordMapper, ApplicationIterationRecord> implements ApplicationIterationRecordService {
    private final ApplicationMapper applicationMapper;
    private final ApplicationIterationContentService applicationIterationContentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(ApplicationIterationRecordParam param) {
        if (param.getId() == null) {
            ApplicationIterationRecord applicationIterationRecord = ObjectUtils.copying(param, ApplicationIterationRecord.class);
            applicationIterationRecord.setId(IdWorker.getId());
            //保存迭代内容
            applicationIterationContentService.saveOrUpdateBatch(applicationIterationRecord.getId(), param.getContentList());
            applicationMapper.update(null, Wrappers.<Application>lambdaUpdate()
                    .set(Application::getUpdateTime, new Date())
                    .set(Application::getVersion, param.getVersion())
                    .eq(Application::getId, applicationIterationRecord.getApplicationId()
                       ));
            return this.save(applicationIterationRecord);
        } else {
            ApplicationIterationRecord applicationIterationRecord = getById(param.getId());
            Assert.isNotFound(Objects.isNull(applicationIterationRecord), "应用迭代记录不存在");
            ObjectUtils.copying(param, applicationIterationRecord);
            applicationIterationContentService.saveOrUpdateBatch(applicationIterationRecord.getId(), param.getContentList());
            applicationMapper.update(null, Wrappers.<Application>lambdaUpdate()
                    .set(Application::getUpdateTime, new Date())
                    .set(Application::getVersion, param.getVersion())
                    .eq(Application::getId, applicationIterationRecord.getApplicationId()
                       ));
            return updateById(applicationIterationRecord);
        }
    }

    @Override
    public Pagination<ApplicationIterationRecordVO> getList(ApplicationIterationRecordQueryParam param) {
        List<Long> applicationIdList = null;
        if (StringUtils.isNotBlank(param.getKeyWord()) || Objects.nonNull(param.getApplicationId())) {
            List<Application> applications = applicationMapper.selectList(Wrappers.<Application>lambdaQuery()
                                                                                  .select(Application::getId)
                                                                                  .eq(Objects.nonNull(param.getApplicationId()), Application::getId, param.getApplicationId())
                                                                                  .like(StringUtils.isNotBlank(param.getKeyWord()), Application::getName, param.getKeyWord()));
            if (CollectionUtils.isEmpty(applications)) {
                return new Pagination<>();
            }
            applicationIdList = applications.stream().map(Application::getId).collect(Collectors.toList());
        }
        Page<ApplicationIterationRecord> iterationRecordPage = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(applicationIdList), ApplicationIterationRecord::getApplicationId, applicationIdList)
                .orderByDesc(ApplicationIterationRecord::getCreateTime)
                .page(PagingUtils.convert(param));
        if (Objects.isNull(iterationRecordPage) || CollectionUtils.isEmpty(iterationRecordPage.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> applicationIds = new HashSet<>();
        Set<Long> idList = new HashSet<>();
        for (ApplicationIterationRecord record : iterationRecordPage.getRecords()) {
            applicationIds.add(record.getApplicationId());
            idList.add(record.getId());
        }
        Map<Long, String> applicationMap = Optional.ofNullable(applicationMapper.selectList(Wrappers.<Application>lambdaQuery()
                                                                                                    .select(Application::getId, Application::getName)
                                                                                                    .in(Application::getId, applicationIds)))
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(Application::getId, Application::getName));
        Map<Long, List<ApplicationIterationContentInfo>> iterationContenMap = applicationIterationContentService
                .getContentByIterationIdList(idList)
                .stream().collect(Collectors.groupingBy(ApplicationIterationContentInfo::getIterationId));
        List<ApplicationIterationRecordVO> result = iterationRecordPage.getRecords()
                .stream()
                .map(t -> {
                    ApplicationIterationRecordVO vo = ObjectUtils.copying(t, ApplicationIterationRecordVO.class);
                    vo.setApplicationName(applicationMap.get(t.getApplicationId()));
                    vo.setContent(iterationContenMap.get(t.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
        return PagingUtils.convert(iterationRecordPage, result);
    }

    @Override
    public String getLastVersion(Long applicationId) {
        ApplicationIterationRecord iterationRecord = this.lambdaQuery()
                .eq(ApplicationIterationRecord::getApplicationId, applicationId)
                .orderByDesc(ApplicationIterationRecord::getCreateTime)
                .last("limit 1")
                .one();

        return Objects.nonNull(iterationRecord) ? iterationRecord.getVersion() : "";
    }

    @Override
    public ApplicationIterationRecordVO getDetail(Long id) {
        ApplicationIterationRecord applicationIterationRecord = this.getById(id);
        Assert.isNotFound(Objects.isNull(applicationIterationRecord), "应用迭代记录不存在");
        ApplicationIterationRecordVO vo = ObjectUtils.copying(applicationIterationRecord, ApplicationIterationRecordVO.class);
        Application application = applicationMapper.selectById(applicationIterationRecord.getApplicationId());
        vo.setApplicationName(Objects.nonNull(application) ? application.getName() : "");
        vo.setContent(applicationIterationContentService.getContentByIterationIdList(Collections.singleton(id)));
        return vo;
    }

    @Override
    public Map<Long, String> getApplicationVersion(List<Long> applicationIds) {
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Collections.emptyMap();
        }
        List<ApplicationIterationRecord> iterationRecords = this.lambdaQuery()
                .select(ApplicationIterationRecord::getApplicationId, ApplicationIterationRecord::getVersion, ApplicationIterationRecord::getCreateTime)
                .in(ApplicationIterationRecord::getApplicationId, applicationIds)
                .list();
        if (CollectionUtils.isEmpty(iterationRecords)) {
            return Collections.emptyMap();
        }
        Map<Long, List<ApplicationIterationRecord>> applicaitionVersionMap = iterationRecords.stream()
                .collect(Collectors.groupingBy(ApplicationIterationRecord::getApplicationId));
        return applicaitionVersionMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, t -> t.getValue().stream()
                        .max(Comparator.comparing(ApplicationIterationRecord::getCreateTime))
                        .map(ApplicationIterationRecord::getVersion).orElse("")));
    }
}
