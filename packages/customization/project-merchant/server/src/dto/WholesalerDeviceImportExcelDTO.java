package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerDeviceImportExcelDTO extends ConvertionDTO<WholesalerDeviceImportExcelDTO, WholesalerDeviceDTO> {

    public WholesalerDeviceImportExcelDTO() {
    }

    public WholesalerDeviceImportExcelDTO(WholesalerDeviceDTO dto) {
        super();
        map(dto);
    }

    public WholesalerDeviceImportExcelDTO(List<WholesalerDeviceDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "档位名称（必填）", width = 30, isNotNull = true)
    private String stallName;

    @Excel(name = "设备类型（必填）", width = 30, isNotNull = true)
    private String deviceType;

    @Excel(name = "设备数量（必填）", width = 30, isNotNull = true)
    private String count;

    @Excel(name = "设备描述", width = 50)
    private String description;

}
