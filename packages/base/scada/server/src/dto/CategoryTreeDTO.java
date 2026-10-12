package com.guhecloud.kaihe.scada.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryTreeDTO {

    private String text;

    private String href;

    private Integer key;

    private Integer pkey;

    private String folder;

    private List<CategoryTreeDTO> nodes;

}
