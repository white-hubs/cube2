package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskGenCirculateTypeEnum;
import lombok.Data;

@Data
public class PlanTaskCirculateConfigDTO {
    private DisinfectionTaskGenCirculateTypeEnum type;
    private Integer day;
    private String time;

    private String lastTime;
}
