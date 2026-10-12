package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StallIdTypeDTO {
    private Long id;
    private String name;
    private String typeId;
    private Set<Long> tagIds;
}
