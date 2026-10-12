package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagRefPageRequest extends Paging {
    private List<Long> ids;
    private List<Long> tagIds;
    private String name;
    private Long deptId;
    private String typeId;
}
