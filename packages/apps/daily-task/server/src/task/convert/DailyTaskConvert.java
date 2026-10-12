package com.guhecloud.cube.biz.daily.task.task.convert;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyUrgencyPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskUserTypeEnum;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyModelConverterUtil;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 19:16
 * @Description:
 */
public class DailyTaskConvert {
    private DailyTaskConvert() {}

    public static List<DailyTaskPageResult> convertResult(List<DailyTaskPO> records, Map<Long, DailyTaskCategoryPO> categoryMap,
                                                          Map<Long, DailyUrgencyPO> urgencyPOMap,
                                                          Map<Long, UserDTO> userMap,
                                                          Map<Long, DailySuperintendentDTO> userDTOMap,
                                                          Map<Long, List<DailyTaskUserPO>> dailyTaskUserMap,
                                                          Map<Long, List<DailyTaskTargetPO>> taskTargetMap,
                                                          Map<String, List<String>> noCompleteStatusMap
                                                         ) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        List<DailyTaskPageResult> result = new ArrayList<>();
        for (DailyTaskPO record : records) {
            DailyTaskPageResult resultItem = new DailyTaskPageResult();
            resultItem.setId(record.getId());
            resultItem.setTaskName(record.getName());
            resultItem.setEndTime(record.getEndTime());
            resultItem.setProgress(record.getProgress());
            resultItem.setTargetCount(record.getTargetCount());
            UserDTO userDTO = userMap.getOrDefault(record.getId(), new UserDTO());
            resultItem.setPublishUser(userDTO.getName());
            resultItem.setPublishDepartment(Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getName() : null);
            resultItem.setStartTime(record.getCreateTime().toString());
            resultItem.setTaskCategoryName(categoryMap.getOrDefault(record.getTaskCategoryId(), new DailyTaskCategoryPO()).getName());
            resultItem.setUrgencyName(urgencyPOMap.getOrDefault(record.getUrgencyId(), new DailyUrgencyPO()).getName());
            resultItem.setStatus(record.getStatus());
            resultItem.setDeadline(record.getDeadline());
            resultItem.setTaskUserName(userDTOMap.getOrDefault(record.getId(), new DailySuperintendentDTO()).getTargetName());
            resultItem.setMaster(Objects.isNull(record.getParentId()));
            resultItem.setContinuity(record.getContinuity());
            setButtonPermission(taskTargetMap, noCompleteStatusMap, record,
                                dailyTaskUserMap.getOrDefault(record.getId(), new ArrayList<>()), resultItem);
            result.add(resultItem);
        }
        return result;
    }

    private static void setButtonPermission(Map<Long, List<DailyTaskTargetPO>> taskTargetMap,
                                            Map<String, List<String>> noCompleteStatusMap,
                                            DailyTaskPO record,
                                            List<DailyTaskUserPO> taskUsers,
                                            DailyTaskPageResult resultItem
                                           ) {
        Optional<DailyTaskUserPO> assignee = taskUsers.stream()
                .filter(taskUser -> DailyTaskUserTypeEnum.ASSIGNEE.name().equals(taskUser.getTaskUserType())
                                    && Objects.equals(SecurityContextHolder.getPrincipal().getId(), taskUser.getTargetId()))
                .findFirst();
        if (assignee.isEmpty()) {
            resultItem.setAllowTransfer(false);
            return;
        }
        List<String> noCompleteStatus = noCompleteStatusMap.getOrDefault(record.getDailyType(), new ArrayList<>());
        if (!DailyTaskTypeEnum.TARGET.name().equals(record.getTaskType())) {
            resultItem.setAllowTransfer(noCompleteStatus.contains(record.getStatus()));
            return;
        }
        long count = taskTargetMap.getOrDefault(record.getId(), new ArrayList<>()).stream()
                .filter(target -> noCompleteStatus.contains(target.getStatus())
                                  && (Objects.isNull(target.getTaskId()) || Objects.equals(target.getTaskId(), record.getId())))
                .count();
        resultItem.setAllowTransfer(noCompleteStatus.contains(record.getStatus()) && count > 0);
    }

    public static DailyTaskDetailResult convertResult(DailyTaskPO po) {
        var result = DailyModelConverterUtil.dailyTaskPoToResult(po);
        result.setDescription(po.getDescription());
        result.setStatus(po.getStatus());
        result.setTaskCategoryId(po.getTaskCategoryId());
        result.setUrgencyId(po.getUrgencyId());
        result.setFileRefKey(po.getFileRefKey());
        result.setStartTime(po.getStartTime());
        result.setDeadline(po.getDeadline());
        result.setTaskDay(po.getTaskDay());
        result.setTaskHour(po.getTaskHour());
        result.setTaskMinute(po.getTaskMinute());
        result.setDescription(po.getDescription());
        result.setMethod(po.getMethod());
        result.setIsOnDuty(po.getIsOnDuty());
        result.setRequireProgress(Objects.nonNull(po.getRequireProgress()) ? BigDecimal.valueOf(po.getRequireProgress())
                .divide(BigDecimal.valueOf(po.getTargetCount()), 2, RoundingMode.HALF_UP) : null);
        result.setTaskType(po.getTaskType());
        result.setContinuity(po.getContinuity());
        return result;
    }
}
