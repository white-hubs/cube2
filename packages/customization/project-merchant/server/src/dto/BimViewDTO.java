package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.BimView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BimViewDTO extends ConvertionDTO<BimViewDTO, BimView> {

    public BimViewDTO() {
    }

    public BimViewDTO(BimView bimView) {
        super();
        map(bimView);
    }

    public BimViewDTO(List<BimView> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    @NotNull
    @NotBlank
    private String projectId;

    /**
     * 视觉名称
     */
    @NotNull
    @NotBlank
    @Length(min = 1, max = 40)
    private String name;

    /**
     * 楼层名称
     */
    private String modelLevel;

    /**
     * 专业名称
     */
    private String specialty;

    /**
     * 构建编码
     */
    private String ecode;

    /**
     * 视觉配置
     */
    @NotNull
    @NotBlank
    private String viewconfig;

}
