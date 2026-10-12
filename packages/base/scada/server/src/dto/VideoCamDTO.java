package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VideoCamDTO {

    @JsonProperty("ID")
    private String ID;
    @JsonProperty("Name")
    private String Name;


}
