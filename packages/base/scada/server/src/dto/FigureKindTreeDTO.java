package com.guhecloud.kaihe.scada.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FigureKindTreeDTO {

    private String text;

    private String href;

    private Integer key;

    private Integer pkey;

    private String folder;

    private List<FigureKindTreeDTO> nodes;

}
