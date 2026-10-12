package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/20
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerContractExcelDTO extends ConvertionDTO<WholesalerContractExcelDTO, WholesalerContractDTO> {
    public WholesalerContractExcelDTO() {}

    public WholesalerContractExcelDTO(WholesalerContractDTO contract) {
        super();
        map(contract);
    }

    public WholesalerContractExcelDTO(List<WholesalerContractDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "合同编号", width = 30)
    private String code;

    @Excel(name = "签约方式（0新签约，1续签）", width = 30)
    private Integer signingWay;

    @Excel(name = "合同类型", width = 30)
    private String contractType;

    private WholesalerContractTypeDTO contractTypeDto;

    @Excel(name = "档位", width = 30)
    private String stallName;

    private List<WholeSpaceDTO> stallDtos;

    @Excel(name = "合同状态（0草稿 1乙方签约 2签章（生效））", width = 30)
    private Integer status;

    @Excel(name = "创建人", width = 30)
    private String createBy;

    @Excel(name = "创建时间", width = 30)
    private String createTime;

}
