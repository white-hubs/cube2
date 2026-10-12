package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionTaskCheckInMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskCheckIn;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.p.TaskCheckInPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskCheckInService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCheckInVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisinfectionTaskCheckInServiceImpl extends ServiceImpl<DisinfectionTaskCheckInMapper, DisinfectionTaskCheckIn> implements DisinfectionTaskCheckInService {
    private final FileClient fileClient;

    /**
     * 获取打卡记录
     *
     * @param param 分页条件
     *
     * @return 分页数据
     */
    @Override
    public Pagination<TaskCheckInVO> pageCheckIn(TaskCheckInPageParam param) {
        // 查询打卡记录
        var page = this.page(PagingUtils.convert(param), Wrappers.<DisinfectionTaskCheckIn>lambdaQuery()
                .eq(param.getProjectId() != null, DisinfectionTaskCheckIn::getProjectId, param.getProjectId())
                .eq(param.getTaskId() != null, DisinfectionTaskCheckIn::getTaskId, param.getTaskId())
                .like(StringUtils.isNotBlank(param.getUserName()), DisinfectionTaskCheckIn::getCheckUserName, param.getUserName()));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var pagination = PagingUtils.convert(page, TaskCheckInVO.class);
        /* 提取文件id */
        Set<Long> fileIdSet = new HashSet<>();
        pagination.getRecords().stream().filter(record -> StringUtils.isNotBlank(record.getFiles()))
                .forEach(record -> StringUtils.splitAndIgnoreBlank(record.getFiles())
                        .forEach(fileId -> fileIdSet.add(Long.parseLong(fileId))));
        if (CollectionUtils.isEmpty(fileIdSet)) {
            return pagination;
        }
        // 获取文件
        List<FileVO> fileVOList = fileClient.getByIds(new ArrayList<>(fileIdSet));
        if (CollectionUtils.isEmpty(fileVOList)) {
            return pagination;
        }
        var fileMap = fileVOList.stream().collect(Collectors.toMap(FileVO::getId, fileVO -> fileVO, (o, n) -> o));
        // 填充文件
        pagination.getRecords().forEach(record -> record.setFileVOList(this.handleFileVOList(record.getFiles(), fileMap)));
        return pagination;
    }

    private List<FileVO> handleFileVOList(String fileIds, Map<Long, FileVO> fileVOMap) {
        if (StringUtils.isBlank(fileIds)) {
            return null;
        }
        return StringUtils.splitAndIgnoreBlank(fileIds).stream().filter(Objects::nonNull)
                .map(fileId -> fileVOMap.get(Long.parseLong(fileId)))
                .collect(Collectors.toList());
    }
}
