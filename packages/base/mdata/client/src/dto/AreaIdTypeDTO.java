package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AreaIdTypeDTO {
    private Long id;
    private String name;
    private String typeId;
    private List<Long> tagIds;
}
