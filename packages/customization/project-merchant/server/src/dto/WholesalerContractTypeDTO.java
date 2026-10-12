package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContractType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/13
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerContractTypeDTO extends ConvertionDTO<WholesalerContractTypeDTO, WholesalerContractType> {

    public WholesalerContractTypeDTO() {}

    public WholesalerContractTypeDTO(WholesalerContractType type) {
        super();
        map(type);
    }

    public WholesalerContractTypeDTO(List<WholesalerContractType> list) {
        super();
        map(list);
    }

    private String id;

    private String code;

    private String projectId;

    private Integer enabled;

    private String name;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;
}
