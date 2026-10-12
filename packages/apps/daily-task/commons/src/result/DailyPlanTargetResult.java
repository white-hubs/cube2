package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanTargetResult {
    /**
     * 关联id，任务id或者计划ID
     */
    private Long refId;
    /**
     * 对象类别
     * (设备/档位/现场点/资产/风险源/小散工程)
     */
    private String targetCategory;
    private Long targetId;
    private String targetName;
    private String tagIds;
    private String tagNames;
    /**
     * 对象状态
     */
    private String targetStatus;

    /**
     * 对象绑定规则信息
     */
    private DailyParticipateConditionResult participateCondition;
    /**
     * 对象的执行内容
     */
    private List<DailyStandardResultDTO> standardList;
}
