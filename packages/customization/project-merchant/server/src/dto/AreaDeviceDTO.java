package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaDeviceDTO extends ConvertionDTO<AreaDeviceDTO, AreaDeliver> {

    public AreaDeviceDTO() {
    }

    public AreaDeviceDTO(AreaDeliver area) {
        super();
        map(area);
    }

    public AreaDeviceDTO(List<AreaDeliver> list) {
        super();
        map(list);
    }

    private String id;

    private String pid;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    @NotNull
    @NotBlank
    private String level;

    @NotNull
    @NotBlank
    private String type;

    @NotNull
    private Integer sort;

    private List<AreaDeviceDTO> children;

    private int count;

    private List<DeviceDTO> devices;

}

