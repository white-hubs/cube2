package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyResultMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyResultResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 执行结果
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyResultServiceImpl extends ServiceImpl<DailyResultMapper, DailyResultPO> implements DailyResultService {

    private final DailyItemResultRelationService dailyItemResultRelationService;
    private final DailyStandardItemRelationService dailyStandardItemRelationService;

    /**
     * 新增执行结果项
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyResult(DailyResultInsertParam param) {
        var dailyResultPO = new DailyResultPO();
        dailyResultPO.setId(IdWorker.getId());
        dailyResultPO.setName(param.getName());
        dailyResultPO.setEnable(param.getEnable());
        dailyResultPO.setDailyType(param.getDailyType());
        if (param.getResultLabelList() != null) {
            //设置结果项标签
            param.getResultLabelList().forEach(resultLabelDTO -> {
                resultLabelDTO.setValue(IdWorker.getId());
            });
            dailyResultPO.setResultLabelJson(JsonUtils.serialize(param.getResultLabelList()));
        }
        this.save(dailyResultPO);
        return dailyResultPO.getId();
    }

    /**
     * 删除执行结果项
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyResult(Long id) {
        var dailyResultPO = this.getById(id);
        Assert.isNotFound(dailyResultPO, "未查询到相关信息");

        // 检查是否有标准项引用了这个结果
        var itemResultRelationsExist = dailyItemResultRelationService.lambdaQuery()
                .eq(DailyItemResultRelationPO::getResultId, id)
                .exists();
        Assert.isConflict(itemResultRelationsExist, "该结果已被标准项引用，无法删除");
        //检查是否有标准引用
        var standardResult = dailyItemResultRelationService.lambdaQuery()
                .like(DailyItemResultRelationPO::getResultId, id)
                .exists();
        Assert.isConflict(standardResult, "该结果已被标准引用，无法删除");

        return this.removeById(id);
    }

    /**
     * 修改执行结果项
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyResult(DailyResultUpdateParam param) {
        var dailyResultPO = this.getById(param.getId());
        Assert.isNotFound(dailyResultPO, "未查询到相关信息");
        if (param.getName() != null) {
            dailyResultPO.setName(param.getName());
        }
        if (param.getEnable() != null) {
            dailyResultPO.setEnable(param.getEnable());
        }
        if (param.getDailyType() != null) {
            dailyResultPO.setDailyType(param.getDailyType());
        }
        if (param.getResultLabelList() != null) {
            checkResultLabelUseState(param, dailyResultPO);
            param.getResultLabelList().stream()
                    .filter(resultLabelDTO -> resultLabelDTO.getValue() == null)
                    .forEach(resultLabelDTO -> {
                        resultLabelDTO.setValue(IdWorker.getId());
                    });
            dailyResultPO.setResultLabelJson(JsonUtils.serialize(param.getResultLabelList()));
        }
        return this.updateById(dailyResultPO);
    }

    private void checkResultLabelUseState(DailyResultUpdateParam param, DailyResultPO dailyResultPO) {
        var oldResultLabels = JsonUtils.deserialize(dailyResultPO.getResultLabelJson(), new TypeReference<List<DailyResultLabelDTO>>() {});
        //获取删除的结果项
        var reminderLabelValues = param.getResultLabelList().stream().map(DailyResultLabelDTO::getValue).filter(Objects::nonNull)
                .toList();
        if (CollectionUtils.isNotEmpty(oldResultLabels)) {
            var removeLabelValues = oldResultLabels.stream()
                    .filter(dto -> !reminderLabelValues.contains(dto.getValue())).collect(Collectors.toSet());
            //查询这个结果被那些执行项引用
            var itemResultRelations = dailyItemResultRelationService.lambdaQuery()
                    .eq(DailyItemResultRelationPO::getResultId, param.getId()).list();
            //查询这个结果被那些标准引用
            var standardItemResults = dailyStandardItemRelationService.lambdaQuery()
                    .eq(DailyStandardItemResultRelationPO::getResultId, param.getId()).list();
            var useValues = new HashSet<Long>();

            if (CollectionUtils.isNotEmpty(itemResultRelations)) {
                for (DailyItemResultRelationPO itemResultRelation : itemResultRelations) {
                    var value = Long.parseLong(itemResultRelation.getDefaultResultItem());
                    //添加使用的默认结果项
                    useValues.add(value);
                    //添加使用的必须备注结果项
                    useValues.addAll(Arrays.stream(itemResultRelation.getMustRemarkResultItems().split(",")).map(Long::parseLong)
                                             .collect(Collectors.toSet()));
                    //添加使用的必须拍照结果项
                    useValues.addAll(Arrays.stream(itemResultRelation.getMustTakePhotoResultItems().split(",")).map(Long::parseLong)
                                             .collect(Collectors.toSet()));
                }
            }
            if (CollectionUtils.isNotEmpty(standardItemResults)) {
                for (DailyStandardItemResultRelationPO standardItemResult : standardItemResults) {
                    var value = Long.parseLong(standardItemResult.getDefaultResultItem());
                    //添加使用的默认结果项
                    useValues.add(value);
                    //添加使用的必须备注结果项
                    useValues.addAll(Arrays.stream(standardItemResult.getMustRemarkResultItems().split(",")).map(Long::parseLong)
                                             .collect(Collectors.toSet()));
                    //添加使用的必须拍照结果项
                    useValues.addAll(Arrays.stream(standardItemResult.getMustTakePhotoResultItems().split(",")).map(Long::parseLong)
                                             .collect(Collectors.toSet()));
                }
            }
            //查询删除的结果项是不是引用
            if (CollectionUtils.isNotEmpty(removeLabelValues)) {
                removeLabelValues.forEach(dto -> {
                    Assert.isConflict(useValues.contains(dto.getValue()), String.format("【%s】结果项被引用，无法删除", dto.getLabel()));
                });
            }

        }
    }

    /**
     * 查询执行结果项分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyResultResponse> queryDailyResultPage(DailyResultPageParam param) {
        var page = this.page(
                PagingUtils.convert(param),
                Wrappers.<DailyResultPO>lambdaQuery()
                        .like(StringUtils.isNoneBlank(param.getName()), DailyResultPO::getName, param.getName())
                        .eq(Objects.nonNull(param.getEnable()), DailyResultPO::getEnable, param.getEnable())
                        .eq(StringUtils.isNoneBlank(param.getDailyType()), DailyResultPO::getDailyType, param.getDailyType())
                        .orderByDesc(DailyResultPO::getCreateTime)
                            );
        var pagination = PagingUtils.convert(page, DailyResultResponse.class);
        // 将 JSON 字符串转换为 List<DailyResultLabelDTO>
        var records = page.getRecords();
        for (int i = 0; i < pagination.getRecords().size() && i < records.size(); i++) {
            var response = pagination.getRecords().get(i);
            var dailyResultPO = records.get(i);
            if (response != null && dailyResultPO != null && StringUtils.isNoneBlank(dailyResultPO.getResultLabelJson())) {
                try {
                    var resultLabelList = JsonUtils.deserialize(
                            dailyResultPO.getResultLabelJson(),
                            new TypeReference<List<DailyResultLabelDTO>>() {}
                                                               );
                    response.setResultLabelList(resultLabelList);
                } catch (Exception e) {
                    log.warn("反序列化 resultLabelJson 失败: {}", dailyResultPO.getResultLabelJson(), e);
                }
            }
        }
        return pagination;
    }
}
