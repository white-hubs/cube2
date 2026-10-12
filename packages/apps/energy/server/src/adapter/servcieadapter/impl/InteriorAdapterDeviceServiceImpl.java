package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.impl;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.vo.EnergyAreaVo;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ItemDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ItemQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.EnergyDevicePropParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AreaListVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AttributeHistoryVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InteriorAdapterDeviceServiceImpl implements AdapterDeviceService {
    private final AreaClient areaClient;
    private final IotProductClient iotProductClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final IotDeviceClient iotDeviceClient;
    private final MeterDeviceMapper meterDeviceMapper;

    @Override
    public List<ItemDTO> category(ItemQueryParam param) {
        ItemQueryParam itemQueryParam = new ItemQueryParam();
        List<ItemDTO> category = iotProductClient.category(itemQueryParam);
        return category;
    }

    @Override
    public List<KeyValueWithTsDTO> deviceProp(EnergyDevicePropParam devicePropParam) {
        DevicePropHistoryParam devicePropHistoryParam = new DevicePropHistoryParam();
        List<String> propCodes = devicePropParam.getPropCodes();
        devicePropHistoryParam.setPropCodes(propCodes);
        devicePropHistoryParam.setDeviceId(devicePropParam.getDeviceId());
        devicePropHistoryParam.setMaxRecords(10);
        devicePropHistoryParam.setOrder("desc");
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime offset = DateUtils.offset(date, ChronoUnit.DAYS, -15);
        long stTime = DateUtils.toTimestamp(offset);
        long etTime = DateUtils.toTimestamp(date);
        devicePropHistoryParam.setStartTs(stTime);
        devicePropHistoryParam.setEndTs(etTime);
        List<KeyValueWithTsDTO> keyValueWithTsDTOS = iotDevicePropClient.devicePropHistoryProp(devicePropHistoryParam);
        return keyValueWithTsDTOS;
    }

    @Override
    public DeviceDetailDTO detail(DeviceDetailParam deviceDetailParam) {

        return iotDeviceClient.detail(deviceDetailParam);
    }

    @Override
    public List<AreaListVO> areaList() {
        List<EnergyAreaVo> energyAreaVos = (List<EnergyAreaVo>) areaClient.innerList();
        List<AreaListVO> allAreaNodes = ObjectUtils.copying(energyAreaVos, AreaListVO.class);
        List<AreaListVO> topNodes = allAreaNodes.stream().filter(s -> StringUtils.isEmpty(s.getParentId())).collect(Collectors.toList());
        List<AreaListVO> recursive = recursive(allAreaNodes, topNodes);
        this.areaSort(recursive, 0);
        return recursive;
    }

    private void areaSort(List<AreaListVO> areaList, int loop) {
        if (CollectionUtils.isEmpty(areaList) || loop > 100000) {
            return;
        }
        areaList.sort(Comparator.comparing(AreaListVO::getSort, Comparator.nullsLast(Integer::compareTo)));
        areaList.forEach(vo -> this.areaSort(vo.getChildren(), loop + 1));
    }

    @Override
    public List<AreaListVO> areaWithDeviceList() {
        List<AreaListVO> areaListVOS = this.areaList();
        //this.getDeviceInArea(areaListVOS);
        return areaListVOS;
    }

    private void getDeviceInArea(List<AreaListVO> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.stream().forEach(area -> {
                List<AreaListVO> children = area.getChildren();
                if (!CollectionUtils.isEmpty(children)) {
                    this.getDeviceInArea(children);
                } else {
                    children = new ArrayList<>();
                    area.setChildren(children);
                }
                //获取区域下的设备
                List<AreaListVO> deviceByAreaId = meterDeviceMapper.getDeviceByAreaId(area.getId());
                children.addAll(deviceByAreaId);
            });
        }
    }

    @Override
    public List<AttributeHistoryVo> getEnergyMetersAttributeHistory(String deviceId, String properties, Integer step, String start,
                                                                    String end
                                                                   ) throws URISyntaxException {
        return null;
    }

    @Override
    public Pagination<KeyValueWithTsDTO> historyPropPage(DevicePropHistoryPageParam historySampleParam) {
        return iotDevicePropClient.historyPropPage(historySampleParam);
    }

    @Override
    public DeviceTSDTO historySampleProp(HistorySampleParam historySampleParam) {
        return iotDevicePropClient.historySampleProp(historySampleParam);
    }

    @Override
    public Map<String, String> devicePropName(String deviceId) {
        return iotDeviceClient.devicePropName(deviceId);
    }

    private static List<AreaListVO> recursive(List<AreaListVO> allAreaNodes, List<AreaListVO> topNodes) {
        for (AreaListVO areaListVO : topNodes) {
            List<AreaListVO> tree = getChild(areaListVO, allAreaNodes);
            areaListVO.setChildren(tree);
        }
        return topNodes;
    }

    private static List<AreaListVO> getChild(AreaListVO parent, List<AreaListVO> allAreaNodes) {
        //获取子节点
        List<AreaListVO> children = allAreaNodes.stream().filter(m -> m.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());
        children.stream()
                .forEach(m -> m.setChildren(getChild(m, allAreaNodes)));
        return children;
    }
}
