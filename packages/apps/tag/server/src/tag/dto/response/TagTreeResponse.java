package com.guhecloud.cube.srv.gen.tag.tag.dto.response;

import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TagTreeResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private LocalDateTime updateTime;
    private Long level;
    private List<TagTreeResponse> childList;
    private List<OrganizationDTO> departments;
    private List<UserDTO> userDTOList;

    public static TagTreeResponse convert(TagDetailResponse tagDetailResponse, long level, List<TagTreeResponse> childList) {
        TagTreeResponse tagTreeResponse = new TagTreeResponse();
        tagTreeResponse.setLevel(level);
        tagTreeResponse.setId(tagDetailResponse.getId());
        tagTreeResponse.setName(tagDetailResponse.getName());
        tagTreeResponse.setDescription(tagDetailResponse.getDescription());
        tagTreeResponse.setParentId(tagDetailResponse.getParentId());
        tagTreeResponse.setUpdateTime(tagDetailResponse.getUpdateTime());
        tagTreeResponse.setChildList(childList);
        return tagTreeResponse;
    }
}
