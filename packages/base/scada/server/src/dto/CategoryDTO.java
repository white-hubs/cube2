package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.guhecloud.kaihe.scada.server.entity.Category;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CategoryDTO extends ConvertionDTO<CategoryDTO, Category> {

    public CategoryDTO() {
    }

    public CategoryDTO(Category category) {
        super();
        map(category);
    }

    public CategoryDTO(List<Category> list) {
        super();
        map(list);
    }

    private Integer ID;

    @NotNull
    @NotBlank
    private String Name;

    private Integer ParentID;

    private Integer projectId = 0;

}
