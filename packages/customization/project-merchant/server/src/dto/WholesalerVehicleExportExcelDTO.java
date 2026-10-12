package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2021/12/31
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerVehicleExportExcelDTO extends ConvertionDTO<WholesalerVehicleExportExcelDTO, WholesalerVehicleDTO> {
    public WholesalerVehicleExportExcelDTO() {}

    public WholesalerVehicleExportExcelDTO(WholesalerVehicleDTO dto) {
        super();
        map(dto);
    }

    public WholesalerVehicleExportExcelDTO(List<WholesalerVehicleDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "档位", width = 30)
    private String stallName;

    @Excel(name = "车辆牌号", width = 30)
    private String vehiclePlate;

    @Excel(name = "车类型", width = 30)
    private String vehicleType;

    @Excel(name = "颜色", width = 30)
    private String color;

    @Excel(name = "车主", width = 30)
    private String owner;

    @Excel(name = "电话", width = 30)
    private String mobile;

}
