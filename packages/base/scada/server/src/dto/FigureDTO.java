package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.guhecloud.kaihe.scada.server.entity.Figure;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class FigureDTO extends ConvertionDTO<FigureDTO, Figure> {

    public FigureDTO() {
    }

    public FigureDTO(Figure figure) {
        super();
        map(figure);
    }

    public FigureDTO(List<Figure> list) {
        super();
        map(list);
    }

    private Integer ID;

    private Integer FigureKindID;

    private String FolderName;

    private String FigureName;

    private Integer FigureFunctionID;

    private String FunctionName;

    private String FileName;

    private Integer projectId;

}
