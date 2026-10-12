package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSecondPart;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/14
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerSecondPartDTO extends ConvertionDTO<WholesalerSecondPartDTO, WholesalerSecondPart> {

    public WholesalerSecondPartDTO() {}

    public WholesalerSecondPartDTO(WholesalerSecondPart part) {
        super();
        map(part);
    }

    public WholesalerSecondPartDTO(List<WholesalerSecondPart> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    private String contractId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String postCode;

    @NotNull
    @NotBlank
    private String mobile;

    @NotNull
    @NotBlank
    private String companyCode;

    @NotNull
    @NotBlank
    private String legalPerson;

    @NotNull
    @NotBlank
    private String identityCard;

    @NotNull
    @NotBlank
    private String signatory;

    @NotNull
    @NotBlank
    private String signatoryMobile;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    private Integer status;
}
