package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 标签关联列表查询请求（不分页）
 */
@Data
public class TagRefListRequest {
    private List<Long> tagIds;
    private String name;
    private Long deptId;
    private TagRefTypeEnum targetType;
}

