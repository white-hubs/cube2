package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceVideobox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceVideoboxDTO extends ConvertionDTO<DeviceVideoboxDTO, DeviceVideobox> {

    public DeviceVideoboxDTO() {

    }

    public DeviceVideoboxDTO(DeviceVideobox deviceVideobox) {
        super();
        map(deviceVideobox);
    }

    public DeviceVideoboxDTO(List<DeviceVideobox> deviceVideoboxes) {
        super();
        map(deviceVideoboxes);
    }

    private String id;

    private String projectId;

    private String typeId;

    private String code;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime productionDate;

    private String manufacturer;

    private String specification;

    private String contactPerson;

    private String contactPhone;

    private String contactAddress;

    private List<String> liveActionIds;

    private List<String> contractIds;

    private List<FileVO> liveActionFiles;

    private List<FileVO> contractFiles;

}
