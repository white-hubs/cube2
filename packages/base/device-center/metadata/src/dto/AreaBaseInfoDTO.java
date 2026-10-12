package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2024/1/16 20:24
 * @description
 */
@Data
@NoArgsConstructor
public class AreaBaseInfoDTO extends IdToName {
    private String pathName;
    private String pathId;

    public AreaBaseInfoDTO(Long id, String name, String pathId) {
        super(id, name);
        this.pathId = pathId;
    }

    public AreaBaseInfoDTO(Long id, String name, String pathId, String pathName) {
        super(id, name);
        this.pathId = pathId;
        this.pathName = pathName;
    }
}
