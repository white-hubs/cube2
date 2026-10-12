package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScenePointIdTypeDTO {
    private Long id;
    private String name;
    private String typeId;
    private Set<Long> tagIds;
}
