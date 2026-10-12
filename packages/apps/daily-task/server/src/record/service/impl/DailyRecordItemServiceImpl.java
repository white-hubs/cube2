package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordItemPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordItemMapper;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordItemResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordItemService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyRecordItemServiceImpl extends ServiceImpl<DailyRecordItemMapper, DailyRecordItemPO> implements DailyRecordItemService {
    private final FileClient fileClient;

    /**
     * 获取记录项详情
     *
     * @param taskId 任务ID
     *
     * @return 日常记录项结果列表
     */
    @Override
    public List<DailyRecordItemResult> getRecordItemDetail(Long taskId) {

        /* 查询该记录下的所有执行标准项 */
        var recordItem = this.lambdaQuery().eq(DailyRecordItemPO::getTaskId, taskId).list();
        return getDailyRecordItemResults(recordItem);
    }

    /**
     * 根据记录项PO列表获取日报记录项结果列表
     *
     * @param recordItem 日报记录项PO列表
     *
     * @return 日报记录项结果列表
     */
    @Override
    public List<DailyRecordItemResult> getDailyRecordItemResults(List<DailyRecordItemPO> recordItem) {
        // 提取文件引用key并构建日报记录项结果列表
        var fileRefSet = new HashSet<Long>();
        var recordItemResultList = Optional.ofNullable(recordItem).orElse(new ArrayList<>()).stream()
                .map(item -> {
                    fileRefSet.add(item.getFileRefKey());
                    return DailyRecordItemResult
                            .builder().record(item.getRecordId()).itemId(item.getItemId()).itemName(item.getItemName())
                            .resultId(item.getResultId()).resultName(item.getResultName())
                            .errorFlag(item.getErrorFlag()).fileRefKey(item.getFileRefKey())
                            .remark(item.getRemark())
                            .build();
                }).collect(Collectors.toList());

        // 根据文件引用key集合批量获取文件信息
        Map<Long, List<FileVO>> fileMap;
        if (CollectionUtils.isNotEmpty(fileRefSet)) {
            var fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(fileRefSet));
            fileMap = fileClient.getByBusinessKeys(fileParam);
        } else {
            fileMap = new HashMap<>();
        }

        // 将文件信息设置到对应的日报记录项结果中
        return recordItemResultList.stream().peek(item -> item.setFileList(fileMap.get(item.getFileRefKey()))).collect(Collectors.toList());
    }

}
