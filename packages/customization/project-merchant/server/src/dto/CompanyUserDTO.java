package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商铺-用户关联
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserDTO extends ConvertionDTO<CompanyUserDTO, CompanyUser> {

    public CompanyUserDTO() {
    }

    public CompanyUserDTO(CompanyUser companyUser) {
        super();
        map(companyUser);
    }

    public CompanyUserDTO(List<CompanyUser> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 商户ID
     */
    private String companyId;

    /**
     * 商户名称
     */
    private String companyName;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 档位名称
     */
    private String stallName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 是否档主
     */
    private Boolean owner;

    private Integer gender;

    private String mobile;

    /**
     * 身份证,创建人，创建时间。2022/1/5 王量添加（上海二期人员列表中，需要显示这三个属性）
     */
    private String identityCard;

    private String createBy;

    private LocalDateTime createTime;

    private List<SpaceDTO> spaceDTOS;

}
