package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行对象入参对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTargetStandardResultParam {
    /**
     * 对象发放规则条件
     */
    private DailyParticipateConditionParam participateCondition;
    private String targetCategory;
    private Long targetId;
    private List<DailyStandardResultDTO> standardList;
}
