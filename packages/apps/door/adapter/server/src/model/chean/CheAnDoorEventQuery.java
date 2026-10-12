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
public class CheAnDoorEventQuery extends CheAnQueryModel {
    //开始时间
    @JsonProperty("IOTimeStart")
    private String ioTimeStart;
    //结束时间
    @JsonProperty("IOTimeEnd")
    private String ioTimeEnd;
}
