package com.guhecloud.cube.biz.mdata.server.area.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.area.dto.MotherlandDistrictTreeDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.MotherlandDistrict;

import java.util.List;
import java.util.Map;

/**
 * 国家三级行政区
 */
public interface MotherlandDistrictService extends IService<MotherlandDistrict> {

    /**
     * 获取树形三级区域
     *
     * @return 区域集合
     */
    List<MotherlandDistrictTreeDTO> getMotherlandDistrictTreeList();

    /**
     * 获取层级+子对象的名称
     *
     * @param level 层级
     *
     * @return map
     */
    Map<String, List<String>> getMotherlandDistrictLevelMap(Integer level);
}
