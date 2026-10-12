package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lanchun
 * @date 2023/12/8 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInstanceListWithAreaLevelVO {

    private List<AreaNode> areaNodes;

    @Data
    public static class AreaNode {
        private Long id;
        private String name;
        private Long parentId;
        private Boolean isArea = false;
        private Integer Level;

        private List<AreaNode> children;
    }

}
