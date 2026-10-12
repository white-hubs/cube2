package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 光明三方部门树节点
 */
@Data
public class GmThirdDeptTreeDTO {
    private Long id;
    private String label;
    private Integer orderNum;
    private String leader;
    private String status;
    private String delFlag;
    private List<GmThirdDeptTreeDTO> children = Collections.emptyList();
}
