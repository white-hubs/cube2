package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

import java.util.List;

@Data
public class TreeIds {
    private Long id;
    private Long parentId;
    private Integer level;
    private List<TreeIds> children;
}
