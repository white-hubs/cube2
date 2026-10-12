package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class FigureKindDTO extends ConvertionDTO<FigureKindDTO, FigureKind> {

    public FigureKindDTO() {
    }

    public FigureKindDTO(FigureKind figureKind) {
        super();
        map(figureKind);
    }

    public FigureKindDTO(List<FigureKind> list) {
        super();
        map(list);
    }

    @NotNull
    private Integer ID;

    @NotNull
    @NotBlank
    private String KindName;

    @NotNull
    @NotBlank
    private String FolderName;

    private Integer ParentID;

    private Integer projectId = 0;

    private Boolean Public = false;

}
