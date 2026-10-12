package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CheAnDoorOpenQuery extends CheAnQueryModel {
    //开始时间
    @JsonProperty("controllerno")
    private String controllerNo;
    //结束时间
    @JsonProperty("doorno")
    private String doorNo;
}
