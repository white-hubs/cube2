package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionDataType;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;

import java.util.List;
import java.util.Map;

public interface DisinfectionDataTypeService extends IService<DisinfectionDataType> {
    /*
     * 根据文件ID获取数据类型名称
     */
    String getTypeName(long dataId);

    /**
     * 获取文件Map
     */
    Map<Long, FileVO> getFileVOMap(List<DisinfectionDataType> disinfectionDataTypeList);

    Map<Long, FileVO> getFileVOMapByKeyPlaceIds(List<Long> keyPlaceTypeIds);

    Long getIdByName(String name, DisinfectionDataTypeEnum dataTypeEnum);

    Map<Long, String> getNamesMap(List<Long> dataTypeIds);

    List<String> listPlaceTypeNames();

    List<DisinfectionDataType> listInNames(List<String> names, DisinfectionDataTypeEnum type);
}