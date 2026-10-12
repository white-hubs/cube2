package com.guhecloud.cube.biz.daily.task.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MigrationPlanCronTypeDTO {
    private String cycleType;
    private String selection;
    private String hour;
    private String type;
}
