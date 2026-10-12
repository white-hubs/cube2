package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionDataTypeMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionDataType;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionDataTypeServiceImpl extends ServiceImpl<DisinfectionDataTypeMapper, DisinfectionDataType>
        implements DisinfectionDataTypeService {
    private final FileClient fileClient;

    @Override
    public String getTypeName(long dataId) {
        DisinfectionDataType dataType = getById(dataId);
        return Objects.isNull(dataType) ? "" : dataType.getDataName();
    }

    @Override
    public Map<Long, FileVO> getFileVOMap(List<DisinfectionDataType> disinfectionDataTypeList) {
        if (CollectionUtils.isEmpty(disinfectionDataTypeList)) {
            return new HashMap<>();
        }
        List<Long> fileIds = disinfectionDataTypeList.stream().filter(t -> Objects.nonNull(t.getIcon()))
                .map(DisinfectionDataType::getIcon)
                .distinct().collect(Collectors.toList());
        Map<Long, FileVO> fileVOMap;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileVOMap = Optional.ofNullable(fileClient.getByIds(fileIds))
                    .orElse(new ArrayList<>())
                    .stream()
                    .collect(Collectors.toMap(FileVO::getId, Function.identity()));
        } else {
            fileVOMap = new HashMap<>();
        }
        return disinfectionDataTypeList.stream()
                .filter(t -> fileVOMap.containsKey(t.getIcon()))
                .map(t -> {
                    FileVO fileVO = fileVOMap.get(t.getIcon());
                    fileVO.setId(t.getId());
                    return fileVO;
                }).collect(Collectors.toMap(FileVO::getId, Function.identity()));
    }

    @Override
    public Map<Long, FileVO> getFileVOMapByKeyPlaceIds(List<Long> keyPlaceTypeIds) {
        if (CollectionUtils.isEmpty(keyPlaceTypeIds)) {
            return Collections.emptyMap();
        }
        return getFileVOMap(listByIds(keyPlaceTypeIds));
    }

    @Override
    public Long getIdByName(String name, DisinfectionDataTypeEnum dataTypeEnum) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        DisinfectionDataType dataType = lambdaQuery().eq(DisinfectionDataType::getDataType, dataTypeEnum)
                .eq(DisinfectionDataType::getDataName, name).one();
        Assert.parameterIsNull(dataType, "消杀数据类型不存在");
        return dataType.getId();
    }

    @Override
    public Map<Long, String> getNamesMap(List<Long> dataTypeIds) {
        if (ListUtils.isEmpty(dataTypeIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionDataType> dataTypeList = lambdaQuery().select(DisinfectionDataType::getId, DisinfectionDataType::getDataName)
                .in(DisinfectionDataType::getId, dataTypeIds).list();
        return MapUtils.listToMap(dataTypeList, DisinfectionDataType::getId, DisinfectionDataType::getDataName);
    }

    @Override
    public List<String> listPlaceTypeNames() {
        List<DisinfectionDataType> dataTypeList = lambdaQuery()
                .select(DisinfectionDataType::getDataName)
                .eq(DisinfectionDataType::getDataType, DisinfectionDataTypeEnum.PLACE)
                .list();
        if (ListUtils.isEmpty(dataTypeList)) {
            return Collections.emptyList();
        }

        return ListUtils.convertItem(dataTypeList, DisinfectionDataType::getDataName);
    }

    @Override
    public List<DisinfectionDataType> listInNames(List<String> names, DisinfectionDataTypeEnum type) {
        if (ListUtils.isEmpty(names)) {
            return Collections.emptyList();
        }
        return lambdaQuery().eq(DisinfectionDataType::getDataType, type).in(DisinfectionDataType::getDataName, names).list();
    }
}