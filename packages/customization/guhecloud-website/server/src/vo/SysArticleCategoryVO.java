package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysArticleCategoryVO {
    private Long id;
    private String categoryName;
    private Long parentId;
    private Integer sort;
    private Integer status;
    private Integer articleCount;
    private List<SysArticleCategoryVO> children;
}
