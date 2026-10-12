package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HandlerService {
    String handlerDeptIdsAndPostIdsToNames(String deptIds, String postIds);

    String handlerUserIdsToNames(String userIds);

    Integer getLimitTotalMinute(String taskTimeLimit);

    String HandlerDeptIdsAndPostIdsToUserIds(String deptIds, String postIds, String roleIds);

    String handlerUserIdsToDeptIds(String userIds);

    String handlerTargetDTOListToTargetIds(List<PlanRelationPO> targetDTOList);

    String handlerInspectTargetDTOListToTargetIds(List<InspectTargetDTO> targetDTOList);

    String handlerTaskTargetDTOListToTargetIds(List<InspectTargetDTO> taskTargetDTOList);

    TaskRateResult handleTaskRateResult(InspectTaskPO po);

    Double getTaskRecordRate(Long taskId, Integer targetNum);

    Long getTaskRecordNum(Long taskId, InspectRecordResultEnum resultEnum);

    List<InspectRecordVO> getTaskRecordList(Long planId, Long taskId);

    String getUserNameByName(String username);

    String getUserNameById(Long userId);

    String getUserNameById(String userId);

    Set<Long> stringIdsToList(String ids);

    List<Long> stringIdsToList(String ids, String CharSequence);

    String jsonDayToStringDay(String jsonString);

    void inspectTaskStatusCheck(List<Long> taskIdList, List<InspectRecordPO> inspectRecordPOList, Principal principal);

    void inspectTaskStatusCheck(List<Long> recordIdList, Principal principal);

    void handlerRepairTodo(List<InspectRecordPO> recordList, Principal principal, LocalDateTime now);

    void sendChangeTodo(Long taskId, InspectTaskStatusEnum nextStatus, String oldStatus, Map<Long, List<InspectRecordPO>> recordMap,
                        Long userId, List<Long> nextUserIds
                       );
}
