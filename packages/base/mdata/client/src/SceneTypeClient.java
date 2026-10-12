package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.SceneTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.SceneTypeQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "mdata")
public interface SceneTypeClient {

    /**
     * 场景列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/scene-type/list")
    List<SceneTypeDTO> listSceneTypes(@SpringQueryMap SceneTypeQueryParam param);

    /**
     * 场景分页列表
     *
     * @return
     */
    @GetMapping("/scene-type/page")
    List<SceneTypeDTO> page();
}
