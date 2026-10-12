package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceIdTypeDTO {
    private Long id;
    private String name;
    private String typeId;
    private Set<Long> tagIds;
}
