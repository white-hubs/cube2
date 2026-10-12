package com.guhecloud.cube.deliver.project.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.AcreageDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import com.guhecloud.cube.deliver.project.merchant.dto.AreaDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MerchantAreaService extends IService<AreaDeliver> {

    public static final String TYPE_BUILDINGS = "areaTypeBuildings";  // 楼栋区域
    public static final String TYPE_OUTDOOR = "areaTypeOutdoor";     // 室外区域


    public static final String LEVEL_BUILDINGS_BUILDING = "areaTypeBuildings_building";
    public static final String LEVEL_BUILDINGS_FLOOR = "areaTypeBuildings_floor";
    public static final String LEVEL_BUILDINGS_SLICE = "areaTypeBuildings_slice";

    public static final String LEVEL_OUTDOOR_FIRST = "areaTypeOutdoor_first";
    public static final String LEVEL_OUTDOOR_SECOND = "areaTypeOutdoor_second";
    public static final String LEVEL_OUTDOOR_THIRD = "areaTypeOutdoor_third";
    
    public static final String BUSINESS_TYPE = "area";

    AreaDTO extendAreaDtoBimInfo(AreaDTO dto);

    void save(AreaDTO areaDTO);

    void update(AreaDTO areaDTO);

    void remove(String id);

    List<String> parsePath(String areaId);

    List<String> parseNames(@RequestParam("areaIds") List<String> areaIds);

    void updateAcreage(String areaId, AcreageDTO acreageDTO);
}
