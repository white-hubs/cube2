package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaDTO extends ConvertionDTO<AreaDTO, AreaDeliver> {

    public AreaDTO() {
    }

    public AreaDTO(AreaDeliver area) {
        super();
        map(area);
    }

    public AreaDTO(List<AreaDeliver> list) {
        super();
        map(list);
    }

    /**
     * ID
     */
    private String id;

    private String pid;

    @NotNull
    @NotBlank
    private String projectId;

    private String bimId;

    private String code;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    private Double acreage;

    @NotNull
    @NotBlank
    private String level;
    private String levelName;

    @NotNull
    @NotBlank
    private String type;
    private String typeName;

    @NotNull
    private Integer sort;

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

    private Integer areasNum;

    private List<AreaDTO> children;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private List<String> names = new ArrayList<>();

    private List<String> ids = new ArrayList<>();

    private BimInfoDTO bimInfo;
    private Integer spaceNum = 0;

}


