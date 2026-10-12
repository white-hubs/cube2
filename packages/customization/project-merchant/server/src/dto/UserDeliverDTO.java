package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.PostDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDeliverDTO extends ConvertionDTO<UserDeliverDTO, UserDeliver> {

    public UserDeliverDTO() {
    }

    public UserDeliverDTO(UserDeliver user) {
        super();
        map(user);
    }

    public UserDeliverDTO(List<UserDeliver> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String projectName;

    private Boolean isManager;

    private String deptId;

    private Boolean isDeptManager;

    private String deptName;

    //    //    private String postId;

    private List<String> postIds;

    private List<PostDTO> postDTOS;

    //    private String postName;

    private List<String> roleIds;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String username;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String nickname;

    private String pinyin;

    private Integer gender;

    @NotNull
    @NotBlank
    private String mobile;

    @Email
    private String email;

    //    @IdCard
    private String identityCard;

    private String avatar;

    private Boolean enabled;

    private String type;

    private String remark;

    private String loginIp;

    private LocalDateTime loginTime;

    private String theme;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private List<String> authDeptIds;

    private List<String> authAreaIds;

    private List<CompanyDTO> companyDTOS;

    private String wxopenid;

    private String storeId;

    private String storeName;

    private String companyName;

    private boolean companyOwner;

}
