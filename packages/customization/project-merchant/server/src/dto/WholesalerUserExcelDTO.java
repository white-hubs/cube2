package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/19
 * @description
 */
@Data
public class WholesalerUserExcelDTO extends ConvertionDTO<WholesalerUserExcelDTO, CompanyUserDTO> {
    public WholesalerUserExcelDTO() {
    }

    public WholesalerUserExcelDTO(CompanyUserDTO companyUserDto) {
        super();
        map(companyUserDto);
    }

    public WholesalerUserExcelDTO(List<CompanyUserDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "商铺名称", width = 20)
    private String companyName;
    @Excel(name = "所在档位", width = 20)
    private String stallName;
    @Excel(name = "人员名称", width = 20)
    private String nickname;
    @Excel(name = "是否档主", width = 20)
    private Boolean owner;
    @Excel(name = "性别", width = 20)
    private Integer gender;
    @Excel(name = "电话", width = 20)
    private String mobile;
    @Excel(name = "身份证", width = 20)
    private String identityCard;
    @Excel(name = "创建人", width = 20)
    private String createBy;
}
