package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.biz.ouaa.client.dto.MenuDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.MenuDeliverDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoDTO extends ConvertionDTO<UserInfoDTO, UserDeliverDTO> {

    public UserInfoDTO() {
    }

    public UserInfoDTO(UserDeliverDTO user) {
        super();
        map(user);
    }

    public UserInfoDTO(List<UserDeliverDTO> list) {
        super();
        map(list);
    }

    private String id;

    private String username;

    private String nickname;

    private String type;

    private Integer gender;

    private String mobile;

    private String email;

    private String identityCard;

    private String avatar;

    private Boolean enabled;

    private String theme;

    private String remark;

    private String wxopenid;

    private List<MenuDeliverDTO> menus;

    private Set<String> functionCodes;

//    private List<UserProjectDTO> projects;

}
