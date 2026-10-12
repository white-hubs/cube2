package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnisInsightChannelListRequest extends AbstractUnisInsightRequest {

    /**
     * 所属设备id
     */
    private String ownerApsId;

    private Integer pageNum = 1;

    private Integer pageSize = 1;

    public Map<String, Object> getParamForGet() {
        Map<String, Object> param = new HashMap<>(3);
        param.put("owner_aps_id", ownerApsId);
        param.put("page_num", pageNum);
        param.put("page_size", pageSize);
        return param;
    }

    @Override
    public String getUrl() {
        return UriComponentsBuilder.fromHttpUrl(getUrl("/api/bss/v1/udm/channel/list").build())
                .queryParam("owner_aps_id", "{owner_aps_id}")
                .queryParam("page_num", "{page_num}")
                .queryParam("page_size", "{page_size}")
                .build()
                .toUriString();
    }
}
