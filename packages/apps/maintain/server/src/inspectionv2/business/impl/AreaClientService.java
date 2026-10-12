package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.google.common.collect.Lists;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 区域查询
 * @date 2023/6/15 18:33
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaClientService {
    private final AreaClient areaClient;

    /**
     * @Author wql
     * @Description 根据区域id查区域名称
     * @Date 16:10 2023/6/6
     **/
    public String getAreaNameById(Long areaId) {
        if (areaId != null) {
            return areaClient.getAreaNamesById(areaId);
        }
        return "";
    }

    /**
     * @Author wql
     * @Description 根据区域ids查所有区域名称
     * @Date 18:05 2023/6/15
     **/
    public String getAreaNamesByIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            List<Long> collect = Arrays.stream(ids.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            if (ObjectUtils.isEmpty(collect)) {
                return "";
            }
            try {
                List<AreaDTO> byIds = areaClient.getByIds(collect);
                StringBuilder sb = new StringBuilder();
                if (ObjectUtils.isNotEmpty(byIds)) {
                    for (AreaDTO byId : byIds) {
                        sb.append(byId.getName()).append("/");
                    }
                }
                return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
            } catch (Exception e) {
                log.error("获取区域失败(getAreaNamesByIds)!{}", e.getMessage(), e);
            }
        }
        return "";
    }

    /**
     * @Author wql
     * @Description 拼接区域名称
     * @Date 15:56 2023/6/6
     **/
    public String getAndJoinAreaName(LinkedList<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return "";
        }
        try {
            List<AreaDTO> areas = areaClient.getByIds(ids);
            StringBuilder sb = new StringBuilder();
            Optional.ofNullable(areas).orElse(Collections.emptyList()).forEach(area -> {
                sb.append(area.getName()).append("-");
            });
            return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
        } catch (Exception e) {
            log.error("获取区域失败(getAndJoinAreaName)！{}", e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取区域得所有上级id（含自己）
     *
     * @param areaId 区域id
     *
     * @return 层级id
     */
    public LinkedList<Long> getAllTierAreaId(Long areaId) {
        LinkedList<Long> linkedList = Lists.newLinkedList();
        final List<Long> parentIds = areaClient.getParentIds(areaId);
        if (ObjectUtils.isNotEmpty(parentIds)) {
            linkedList.addAll(parentIds);
        }
        linkedList.add(areaId);
        return linkedList;
    }

    public Map<Long, String> getAreaNamesByIdsMap(Set<Long> areaIds) {
        if (ObjectUtils.isEmpty(areaIds)) {
            return Collections.emptyMap();
        }
        try {
            List<AreaDTO> areas = areaClient.getByIds(areaIds);
            return Optional.ofNullable(areas).orElse(Collections.emptyList()).stream()
                    .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        } catch (Exception e) {
            log.error("获取区域失败(getAreaNamesByIdsMap)！{}", e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    public String convertAreaName(Map<Long, String> areaNameMap, String areaIdStr) {
        if (StringUtils.isNotBlank(areaIdStr)) {
            List<Long> collect = Arrays.stream(areaIdStr.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (Long areaId : collect) {
                if (areaNameMap.containsKey(areaId)) {
                    sb.append(areaNameMap.get(areaId)).append("/");
                }
            }
            return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
        }
        return "";

    }
}
