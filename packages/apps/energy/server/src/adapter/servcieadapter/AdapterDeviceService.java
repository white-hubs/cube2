package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ItemDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ItemQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.EnergyDevicePropParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AreaListVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AttributeHistoryVo;
import com.guhecloud.platform.base.model.Pagination;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface AdapterDeviceService {

    List<ItemDTO> category(ItemQueryParam param);

    //设备属性
    List<KeyValueWithTsDTO> deviceProp(EnergyDevicePropParam devicePropParam);

    DeviceDetailDTO detail(DeviceDetailParam deviceDetailParam);

    List<AreaListVO> areaList();

    List<AreaListVO> areaWithDeviceList();

    List<AttributeHistoryVo> getEnergyMetersAttributeHistory(String deviceId, String properties, Integer step, String start,
                                                             String end
                                                            ) throws URISyntaxException;

    Pagination<KeyValueWithTsDTO> historyPropPage(DevicePropHistoryPageParam historySampleParam);

    DeviceTSDTO historySampleProp(HistorySampleParam historySampleParam);

    Map<String, String> devicePropName(String deviceId);
}

