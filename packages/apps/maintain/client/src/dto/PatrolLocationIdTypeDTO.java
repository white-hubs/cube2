package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatrolLocationIdTypeDTO {
    private Long id;
    private String name;
    private Long typeId;
    private Set<Long> tagIds;
}
