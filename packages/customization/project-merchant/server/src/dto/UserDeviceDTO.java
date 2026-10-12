package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDevice;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户设备表
 * </p>
 *
 * @author zwb
 * @since 2020-11-07
 */
@Data
public class UserDeviceDTO extends ConvertionDTO<UserDeviceDTO, UserDevice> {
    public UserDeviceDTO() {
    }

    public UserDeviceDTO(UserDevice userDevice) {
        super();
        map(userDevice);
    }

    public UserDeviceDTO(List<UserDevice> userDevices) {
        map(userDevices);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    @NotNull
    @NotBlank
    private String businessType;

    private String userId;

    @NotNull
    @NotBlank
    private String deviceId;

    private Integer sort;

    private LocalDateTime createTime;

    private Device device;

}
