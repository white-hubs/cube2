package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.AreaBimListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mdata")
public interface AreaSceneConfigClient {

    @GetMapping("/area-scene-configs/bim/area/{areaId}")
    AreaBimListDTO getBimArea(@PathVariable("areaId") Long areaId);
}
