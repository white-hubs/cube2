package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 负责人
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySuperintendentDTO {
    private Long id;
    private Long masterId;
    /**
     * 负责人类型 DailySuperintendentTypeEnum
     */
    private String participateType;
    /**
     * 负责对象的id(例如superintendentType类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;
    private String targetName;
}
