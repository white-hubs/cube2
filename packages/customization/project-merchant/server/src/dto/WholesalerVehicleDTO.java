package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerVehicle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2021/12/31
 * @description 批发商车辆DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerVehicleDTO extends ConvertionDTO<WholesalerVehicleDTO, WholesalerVehicle> {

    public WholesalerVehicleDTO() {}

    public WholesalerVehicleDTO(WholesalerVehicle wholesalerVehicle) {
        super();
        map(wholesalerVehicle);
    }

    public WholesalerVehicleDTO(List<WholesalerVehicle> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String vehiclePlate;

    private String vehicleType;
    private String vehicleTypeName;

    private String color;

    private String owner;

    private String mobile;

    private String stallId;

    private String stallName;

    private String companyId;

    private String companyName;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<Long> fileIds;

    private List<FileVO> fileDTOS;

    // 满足前端组件在编辑时回显档位名称
    private List<SpaceDTO> spaceDTOS;

}
