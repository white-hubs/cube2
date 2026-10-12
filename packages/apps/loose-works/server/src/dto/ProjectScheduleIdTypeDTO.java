package com.guhecloud.cube.srv.gen.loose.works.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleIdTypeDTO {
    private Long id;
    private String name;
    private Long typeId;
    private Set<Long> tagIds;
}
