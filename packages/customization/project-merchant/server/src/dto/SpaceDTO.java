package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SpaceDTO extends ConvertionDTO<SpaceDTO, Space> {

    public SpaceDTO() {
    }

    public SpaceDTO(Space space) {
        super();
        map(space);
    }

    public SpaceDTO(List<Space> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    private List<String> areaIds;

    private List<String> areaNames;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    private String type;
    private String typeName;

    private String bimId;

    private String deptId;

    private String deptName;

    private String companyId;

    private String companyName;

    private String code;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String attribute;
    private String attributeName;

    @NotNull
    @NotBlank
    private String tradeModel;
    private String tradeModelName;

    private Double acreage;

    @Length(max = 100)
    private String remark;

    private float buildingArea;

    private float coverArea;

    private float leaseArea;

    private float leaseRealArea;

    private float leasePublicArea;

    private float fireArea;

    private float cleanArea;

    private float parkingArea;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private BimInfoDTO bimInfo;

    private CompanyDTO companyDTO;

}
