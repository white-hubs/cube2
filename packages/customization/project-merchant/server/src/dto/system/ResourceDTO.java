package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.Resource;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ResourceDTO extends ConvertionDTO<ResourceDTO, Resource> {

    public ResourceDTO() {
    }

    public ResourceDTO(Resource resource) {
        super();
        map(resource);
    }

    public ResourceDTO(List<Resource> list) {
        super();
        map(list);
    }

    private String id;

    private String pid;

    private String group;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String type;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String code;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String name;

    @Length(max = 30)
    private String icon;

    @Length(max = 100)
    private String path;

    private String apis;

    @NotNull
    private Integer sort;

    @NotNull
    private Boolean enabled;

    private List<ResourceDTO> children;

}
