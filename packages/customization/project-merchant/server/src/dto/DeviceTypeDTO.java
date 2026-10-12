package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceTypeDTO extends ConvertionDTO<DeviceTypeDTO, DeviceType> {

    public DeviceTypeDTO() {
    }

    public DeviceTypeDTO(DeviceType deviceType) {
        super();
        map(deviceType);
    }

    public DeviceTypeDTO(List<DeviceType> deviceTypes) {
        super();
        map(deviceTypes);
    }

    private String id;

    private String pid;

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    private Integer validityMonths;

    @NotNull
    private Integer sort;

    private List<DeviceTypeDTO> children;

}
