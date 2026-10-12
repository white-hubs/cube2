package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserExcelDTO extends ConvertionDTO<UserExcelDTO, UserDeliver> {

    public UserExcelDTO() {
    }

    public UserExcelDTO(UserDeliver user) {
        super();
        map(user);
    }

    public UserExcelDTO(List<UserDeliver> list) {
        super();
        map(list);
    }

    @Excel(name = "用户名", maxLength = 25, isNotNull = true)
    private String username;

    @Excel(name = "用户名称", maxLength = 15, isNotNull = true)
    private String nickname;

    @Excel(name = "性别", readConverterExp = "1=男,2=女,9=未知", isNotNull = true)
    @NotNull
    private Integer gender;

    @Excel(name = "手机号", maxLength = 11, isNotNull = true)
    @NotNull
    @NotBlank
    private String mobile;

    @Excel(name = "电子邮箱", maxLength = 45)
    @Email
    private String email;

    @Excel(name = "身份证号", maxLength = 18)
    private String identityCard;

    @Excel(name = "备注", maxLength = 100)
    private String remark;

    @Excel(name = "部门", isNotNull = true)
    private String deptName;

    @Excel(name = "岗位", isNotNull = true)
    private String postNames;

}
