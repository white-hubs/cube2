package com.guhecloud.cube.property.management.client.property.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetailIdTypeDTO {
    private Long id;
    private String name;
    private Long typeId;
    private Set<Long> tagIds;
}
