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
public class WholesalerVehicleImportExcelDTO extends ConvertionDTO<WholesalerVehicleImportExcelDTO, WholesalerVehicleDTO> {
    public WholesalerVehicleImportExcelDTO() {}

    public WholesalerVehicleImportExcelDTO(WholesalerVehicleDTO dto) {
        super();
        map(dto);
    }

    public WholesalerVehicleImportExcelDTO(List<WholesalerVehicleDTO> list) {
        super();
        map(list);
    }

    @Excel(name = "档位名称（必填）", width = 30, isNotNull = true)
    private String stallName;

    @Excel(name = "车牌号（必填）", width = 30, isNotNull = true, maxLength = 20)
    private String vehiclePlate;

    @Excel(name = "车辆类型（必填）", width = 30, isNotNull = true)
    private String vehicleType;

    @Excel(name = "颜色（必填）", width = 30, isNotNull = true, maxLength = 10)
    private String color;

    @Excel(name = "车主（必填）", width = 30, isNotNull = true, maxLength = 10)
    private String owner;

    @Excel(name = "电话（必填）", width = 30, isNotNull = true, maxLength = 20)
    private String mobile;

}
