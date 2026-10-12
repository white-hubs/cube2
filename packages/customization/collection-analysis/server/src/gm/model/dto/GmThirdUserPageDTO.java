package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 光明三方用户分页
 */
@Data
public class GmThirdUserPageDTO {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<GmThirdUserDTO> rows = Collections.emptyList();
}
