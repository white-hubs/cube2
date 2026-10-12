package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnisInsightChannelResponse {

    /**
     * 通道国标id
     */
    @JsonProperty("ape_id")
    private String apeId;

    /**
     * 所属设备id
     */
    @JsonProperty("owner_aps_id")
    private String ownerApsId;

    private String name;
}
