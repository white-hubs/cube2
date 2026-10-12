package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.StreamMediaConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "mdata")
public interface StreamMediaConfigClient {

    @GetMapping("/stream-media-config")
    StreamMediaConfigDTO getStreamMediaConfig();
}
