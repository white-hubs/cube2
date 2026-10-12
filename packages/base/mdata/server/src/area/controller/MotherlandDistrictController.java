package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.guhecloud.cube.biz.mdata.server.area.dto.MotherlandDistrictTreeDTO;
import com.guhecloud.cube.biz.mdata.server.area.service.MotherlandDistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 国家三级行政区
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MotherlandDistrictController {
    private final MotherlandDistrictService motherlandDistrictService;

    /**
     * 获取树形三级区域
     *
     * @return 区域集合
     */
    @GetMapping("/motherland-district/tree-list")
    public List<MotherlandDistrictTreeDTO> getMotherlandDistrictTreeList() {
        return motherlandDistrictService.getMotherlandDistrictTreeList();
    }

    /**
     * 获取层级+子对象的名称
     *
     * @param level 层级
     *
     * @return map
     */
    @GetMapping("/motherland-district/level-map")
    public Map<String, List<String>> getMotherlandDistrictLevelMap(@RequestParam("level") Integer level) {
        return motherlandDistrictService.getMotherlandDistrictLevelMap(level);
    }
}
