package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.area;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParkAreaAppService {

    private final AreaClient areaClient;

    public ParkAreaAppService(AreaClient areaClient) {this.areaClient = areaClient;}

    public String getParkingAreaPathNameByPath(String areaPath) {
        if (StringUtils.isEmpty(areaPath)) {
            return StringUtils.EMPTY_STRING;
        }
        var ids = Arrays.stream(areaPath.split("-")).map(Long::parseLong)
                .toList();
        var areaNames = areaClient.getAreaNamesById(ids.getLast());
        if (StringUtils.isNotBlank(areaNames)) {
            areaNames = areaNames.replaceAll("/", "-");
        }
        return areaNames;
    }

    public Map<String, String> getParkingAreaPathNamesByPaths(Set<String> areaPaths) {
        var areas = this.areaClient.listInnerArea(new AreaQueryParam());
        var areaPathMap = new HashMap<String, String>();
        areaPaths.forEach(areaPath -> {
            var optionalArea = areas.stream()
                    .filter(areaListDTO -> areaListDTO.getSequence().equals(areaPath))
                    .findFirst();
            optionalArea.ifPresent(area -> {
                var areaIds = Arrays.stream(area.getSequence().split("-")).map(Long::parseLong)
                        .toList();
                var areaMap = areas.stream().filter(areaListDTO -> areaIds.contains(areaListDTO.getId()))
                        .collect(Collectors.toMap(AreaListDTO::getId, a -> a));
                List<String> areaNames = new ArrayList<>();
                areaIds.forEach(areaId -> {
                    if (areaMap.containsKey(areaId)) {
                        AreaListDTO areaListDTO = areaMap.get(areaId);
                        areaNames.add(areaListDTO.getName());
                    }
                });
                areaPathMap.put(area.getSequence(), String.join("-", areaNames));
            });
        });
        return areaPathMap;
    }
}
