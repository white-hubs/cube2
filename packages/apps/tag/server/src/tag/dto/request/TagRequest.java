package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import com.guhecloud.cube.srv.gen.tag.tag.entity.TagBusiness;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TagRequest {
    @NotBlank(message = "标签名称不能为空")
    private String name;
    private String description;
    private Long parentId;
    private String depIds;
    private List<Long> userIdList;

    public TagBusiness convert(long id) {
        TagBusiness tag = new TagBusiness();
        tag.setId(id);
        tag.setName(name.trim());
        tag.setDescription(description);
        tag.setParentId(parentId);
        tag.setDepIds(depIds);
        return tag;
    }
}
