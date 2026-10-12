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
public class WholesalerDeviceExportExcelDTO extends ConvertionDTO<WholesalerDeviceExportExcelDTO, WholesalerDeviceDTO> {

    public WholesalerDeviceExportExcelDTO() {
    }

    public WholesalerDeviceExportExcelDTO(WholesalerDeviceDTO dto) {
        super();
        map(dto);
    }

    public WholesalerDeviceExportExcelDTO(List<WholesalerDeviceDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "档位", width = 30)
    private String stallName;

    @Excel(name = "设备类型", width = 30)
    private String typeName;

    @Excel(name = "设备数量", width = 30)
    private Integer count;

    @Excel(name = "设备描述", width = 30)
    private String description;

    @Excel(name = "是否危险物品", width = 30, readConverterExp = "0=否,1=是")
    private Integer danger;

}
