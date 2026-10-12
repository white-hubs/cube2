package com.guhecloud.kaihe.scada.server.dto;

import com.guhecloud.kaihe.scada.server.entity.FigureFunction;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import lombok.Data;

import java.util.List;

@Data
public class FigureFunctionDTO extends ConvertionDTO<FigureFunctionDTO, FigureFunction> {

    public FigureFunctionDTO() {
    }

    public FigureFunctionDTO(FigureFunction figureFunction) {
        super();
        map(figureFunction);
    }

    public FigureFunctionDTO(List<FigureFunction> list) {
        super();
        map(list);
    }

    private Integer ID;

    private String FunctionName;

    private String FunctionDesc;

}
