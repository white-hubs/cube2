package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskFeedbackResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskFeedbackPO;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskFeedbackMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskFeedbackService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskFeedbackServiceImpl extends ServiceImpl<DailyTaskFeedbackMapper, DailyTaskFeedbackPO> implements DailyTaskFeedbackService {

    private final FileClient fileClient;
    private final UserClient userClient;

    @Override
    public List<DailyTaskFeedbackResult> getFeedbackListByTaskId(Long taskId) {
        List<DailyTaskFeedbackPO> feedbackList = this.lambdaQuery()
                .eq(DailyTaskFeedbackPO::getTaskId, taskId)
                .orderByAsc(DailyTaskFeedbackPO::getSchedule)
                .orderByAsc(DailyTaskFeedbackPO::getCreateTime)
                .list();
        if (CollectionUtils.isEmpty(feedbackList)) {
            return new ArrayList<>();
        }

        Set<Long> userIdSet = new HashSet<>();
        Set<Long> fileIds = new HashSet<>();
        for (DailyTaskFeedbackPO po : feedbackList) {
            if (StringUtils.isNotBlank(po.getCreatorId())) {
                userIdSet.add(Long.parseLong(po.getCreatorId()));
            }
            if (StringUtils.isNotBlank(po.getFiles())) {
                fileIds.addAll(Arrays.stream(po.getFiles().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet()));
            }
        }

        Map<Long, UserDTO> userMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            UserQuery userQuery = new UserQuery();
            userQuery.setUserIds(userIdSet);
            List<UserDTO> users = Optional.ofNullable(userClient.postList(userQuery))
                    .orElse(new ArrayList<>());
            userMap = users.stream()
                    .collect(Collectors.toMap(UserDTO::getId, Function.identity(), (k1, k2) -> k1));
        }

        Map<Long, FileVO> fileVOMap;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileVOMap = Optional.ofNullable(fileClient.getByIds(new ArrayList<>(fileIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));
        } else {
            fileVOMap = new HashMap<>();
        }

        List<DailyTaskFeedbackResult> results = new ArrayList<>();
        for (DailyTaskFeedbackPO po : feedbackList) {
            DailyTaskFeedbackResult result = new DailyTaskFeedbackResult();
            result.setId(po.getId());
            result.setTaskId(po.getTaskId());
            result.setSchedule(po.getSchedule());
            result.setDescription(po.getDescription());
            result.setFeedbackTime(po.getCreateTime());

            Long creatorId = StringUtils.isNotBlank(po.getCreatorId()) ? Long.parseLong(po.getCreatorId()) : null;
            UserDTO user = creatorId != null ? userMap.getOrDefault(creatorId, new UserDTO()) : new UserDTO();
            result.setUsername(user.getName());
            result.setFeedbackDepId(Optional.ofNullable(user.getOrganization())
                    .orElse(new UserDTO.Organization()).getId());
            result.setFeedbackDepName(Optional.ofNullable(user.getOrganization())
                    .orElse(new UserDTO.Organization()).getName());

            if (StringUtils.isNotBlank(po.getFiles())) {
                List<FileVO> fileVOList = Arrays.stream(po.getFiles().split(","))
                        .filter(t -> fileVOMap.containsKey(Long.parseLong(t)))
                        .map(t -> fileVOMap.get(Long.parseLong(t)))
                        .collect(Collectors.toList());
                result.setFileList(fileVOList);
            }
            results.add(result);
        }
        return results;
    }
}
