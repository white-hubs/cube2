package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CategoryQueryDTO {

    private Integer id;

    private Integer pId;

    private String name;

}
