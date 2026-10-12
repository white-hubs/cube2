package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceElevatorProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceDTO extends ConvertionDTO<DeviceDTO, Device> {

    public DeviceDTO() {
    }

    public DeviceDTO(Device device) {
        super();
        map(device);
    }

    public DeviceDTO(List<Device> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    @NotNull
    @NotBlank
    private String typeId;

    private String typeName;

    private String typeCode;

    private String model;

    private String iotDeviceId;

    private String iotDeviceType;

    private String bimModelId;

    private String code;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    private String pinyin;

    @NotNull
    private String status;

    private Integer online;

    private Double price;

    @Length(max = 100)
    private String installAddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime installDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expireDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime productionDate;

    private Integer timeLimit;

    private String timeLimitStr;

    private Integer maintCycle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime maintDate;

    @Length(max = 50)
    private String manufacturer;

    @Length(max = 50)
    private String specification;

    private String deptId;

    @Length(max = 30)
    private String contactPerson;

    //    @Mobile
    private String contactPhone;

    @Length(max = 50)
    private String contactAddress;

    private String qrCode;

    private String gisLongitude;

    private String gisLatitude;

    private String account;

    private String password;

    private String ipAddr;

    private String port;

    private String token;

    private String videoboxId;

    private String financeCode;

    private String remark;

    private Boolean visibility;

    private String monitoringObject;

    private List<String> nearbyCamIds;

    private List<DeviceDTO> nearbyCamDTOS;

    private String videoboxName;

    private List<String> relatedDrawingIds;

    private List<String> liveActionIds;

    private List<String> purchaseContractIds;

    private List<FileVO> relatedDrawings;

    private List<FileVO> liveAction;

    private List<FileVO> purchaseContract;

    private List<String> areaIds;

    private List<String> areaNames;

    private String deptName;

    private BimInfoDTO bimInfo;

    private DeviceElevatorProperty elevatorProperty;

    private Boolean bindingFireinsp;

    private String fireinspPointId;

    private String fireinspPointType;

    //    //    private FireinspPointDTO fireinspPointDTO;

    private String ratedCurrent;

    private String myFavoriteBusinessType;

    private String myFavoriteId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

}
