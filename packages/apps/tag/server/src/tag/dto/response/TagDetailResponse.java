package com.guhecloud.cube.srv.gen.tag.tag.dto.response;

import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.tag.tag.entity.TagBusiness;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TagDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private LocalDateTime updateTime;
    private List<OrganizationDTO> departments;
    private List<UserDTO> userDTOList;

    public static TagDetailResponse convert(TagBusiness tagBusiness, Map<Long, List<Long>> tagRefUser, List<UserDTO> userDTOList,
                                            List<OrganizationDTO> organizationDTOS
                                           ) {
        var tagDetailResponse = new TagDetailResponse();
        tagDetailResponse.setId(tagBusiness.getId());
        tagDetailResponse.setName(tagBusiness.getName());
        tagDetailResponse.setDescription(tagBusiness.getDescription());
        tagDetailResponse.setParentId(tagBusiness.getParentId());
        tagDetailResponse.setUpdateTime(tagBusiness.getUpdateTime());
        if (tagRefUser.containsKey(tagBusiness.getId())) {
            tagDetailResponse.setUserDTOList(userDTOList.stream().filter(u -> tagRefUser.get(tagBusiness.getId()).contains(u.getId()))
                                                     .collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(tagBusiness.getDepIds()) && CollectionUtils.isNotEmpty(organizationDTOS)) {
            tagDetailResponse.setDepartments(organizationDTOS.stream().filter(o -> Arrays
                    .stream(tagBusiness.getDepIds().split(",")).map(Long::valueOf).toList()
                    .contains(o.getId())).collect(Collectors.toList()));
        }
        return tagDetailResponse;
    }
}
