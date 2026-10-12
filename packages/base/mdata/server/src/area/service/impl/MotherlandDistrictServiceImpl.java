package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.MotherlandDistrictMapper;
import com.guhecloud.cube.biz.mdata.server.area.dto.MotherlandDistrictTreeDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.MotherlandDistrict;
import com.guhecloud.cube.biz.mdata.server.area.service.MotherlandDistrictService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 国家三级行政区
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MotherlandDistrictServiceImpl extends ServiceImpl<MotherlandDistrictMapper, MotherlandDistrict> implements MotherlandDistrictService {

    /**
     * 获取树形三级区域
     *
     * @return 区域集合
     */
    @Override
    public List<MotherlandDistrictTreeDTO> getMotherlandDistrictTreeList() {
        var poList = this.getListPO(3);
        return handlerDistrictTOTree(poList, 1);
    }

    /**
     * 获取层级+子对象的名称
     *
     * @param level 层级
     *
     * @return map
     */
    @Override
    public Map<String, List<String>> getMotherlandDistrictLevelMap(Integer level) {
        Map<String, List<String>> resultMap = new HashMap<>();
        var poList = this.getListPO(level + 1);
        var list = handlerDistrictTOTree(poList, level);
        list.forEach(dto -> {
            var arrays = dto.getChildrenList();
            resultMap.put(dto.getName(),
                          arrays == null ? new ArrayList<>() : arrays.stream().map(MotherlandDistrictTreeDTO::getName).toList());
        });
        return resultMap;
    }

    private List<MotherlandDistrict> getListPO(Integer maxLevel) {
        return this.lambdaQuery().eq(MotherlandDistrict::getStatus, 1).le(maxLevel != null, MotherlandDistrict::getLevel, maxLevel).list();
    }

    private List<MotherlandDistrictTreeDTO> handlerDistrictTOTree(List<MotherlandDistrict> motherlandDistricts, Integer startLevel) {
        List<MotherlandDistrictTreeDTO> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(motherlandDistricts)) {
            return Collections.emptyList();
        }
        var list = ObjectUtils.copying(motherlandDistricts, MotherlandDistrictTreeDTO.class);

        Map<Integer, List<MotherlandDistrictTreeDTO>> treeMap = list.stream()
                .collect(Collectors.groupingBy(MotherlandDistrictTreeDTO::getLevel,
                                               TreeMap::new,
                                               Collectors.toList()));
        for (Map.Entry<Integer, List<MotherlandDistrictTreeDTO>> integerListEntry : treeMap.entrySet()) {
            var key = integerListEntry.getKey();
            var nextList = treeMap.get(key + 1);
            if (CollectionUtils.isNotEmpty(nextList)) {
                Map<Long, List<MotherlandDistrictTreeDTO>> collect = nextList.stream()
                        .collect(Collectors.groupingBy(MotherlandDistrictTreeDTO::getPid));
                integerListEntry.getValue().forEach(dto -> {
                    var childrenList = collect.get(dto.getId());
                    dto.setChildrenList(childrenList);
                    if (Objects.equals(dto.getLevel(), startLevel)) {
                        resultList.add(dto);
                    }
                });
            }
        }
        return resultList;
    }
}
