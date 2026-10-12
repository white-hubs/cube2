package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastDeviceVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.request.GetDevicePropRequest;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.response.DevicePropResponse;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.service.BroadcastIotDevicePropQueryService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.response.BroadcastDeviceResponse;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.service.BroadcastDeviceQueryService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.util.GeoToolUtil;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 广播设备管理接口
 *
 * @author lanchun
 * @date 2023/3/27 14:08
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastDeviceController {

    private final static String WORK_STATUS_PRO_CODE = "WorkStatus";

    private final BroadcastDeviceQueryService broadcastDeviceQueryService;

    private final BroadcastIotDevicePropQueryService broadcastIotDevicePropQueryService;

    /**
     * 广播设备列表
     */
    @GetMapping("/broadcast-devices")
    public List<BroadcastDeviceVO> getBroadcastDevices() {
        //获取广播设备
        List<BroadcastDeviceResponse> broadcastDeviceResponses = this.broadcastDeviceQueryService.queryBroadcastDevices();
        if (CollectionUtils.isEmpty(broadcastDeviceResponses)) {
            return Collections.emptyList();
        }
        List<BroadcastDeviceVO> broadcastDeviceVOS = ObjectUtils.copying(broadcastDeviceResponses, BroadcastDeviceVO.class);
        this.convertBroadcastDeviceStatus(broadcastDeviceVOS);
        return broadcastDeviceVOS;
    }

    /**
     * 获取圈选的广播设备
     *
     * @param lng
     * @param lat
     * @param radius
     *
     * @return
     */
    @GetMapping("/broadcast-devices/circle")
    public List<BroadcastDeviceVO> getBroadcastCircleDevices(@RequestParam double lng, @RequestParam double lat,
                                                             @RequestParam double radius
                                                            ) {
        List<BroadcastDeviceResponse> securityDeviceVos = this.broadcastDeviceQueryService.queryBroadcastDevices();
        if (CollectionUtils.isEmpty(securityDeviceVos)) {
            return Collections.emptyList();
        }
        List<BroadcastDeviceVO> broadcastDeviceVOS = ObjectUtils.copying(securityDeviceVos, BroadcastDeviceVO.class).stream()
                .filter(broadcastDeviceVO -> StringUtils.isNotEmpty(broadcastDeviceVO.getLntAndLat())).toList();
        Polygon circle = GeoToolUtil.createCircle(lng, lat, radius);
        List<BroadcastDeviceVO> responseBroadcastDeviceVOS = broadcastDeviceVOS.stream()
                .filter(d -> {
                    String[] lntAndLats = d.getLntAndLat().split(",");
                    if (lntAndLats.length != 2) {
                        return false;
                    } else {
                        return Objects.nonNull(d.getDeviceStatus()) && d.getDeviceStatus() &&
                               circle.contains(GeoToolUtil.createPoint(Double.parseDouble(lntAndLats[0]), Double.parseDouble(lntAndLats[1])));
                    }
                })
                .collect(Collectors.toList());
        responseBroadcastDeviceVOS.forEach(broadcastDeviceVO -> {
            broadcastDeviceVO.setId(broadcastDeviceVO.getDeviceId());
        });
        return responseBroadcastDeviceVOS;
    }

    public void convertBroadcastDeviceStatus(List<BroadcastDeviceVO> broadcastDeviceVOS) {
        Set<Long> deviceIds = broadcastDeviceVOS.stream().map(BroadcastDeviceVO::getId).collect(Collectors.toSet());
        GetDevicePropRequest request = new GetDevicePropRequest();
        request.setDeviceIds(deviceIds);
        request.setPropCode(WORK_STATUS_PRO_CODE);
        Map<Long, DevicePropResponse> devicePropResponsesMap = this.broadcastIotDevicePropQueryService.getBroadcastDeviceProp(request)
                .stream().collect(Collectors.toMap(DevicePropResponse::getId, obj -> obj, (o, n) -> n));
        broadcastDeviceVOS.forEach(broadcastDeviceVO -> {
            broadcastDeviceVO.setId(broadcastDeviceVO.getDeviceId());
            if (devicePropResponsesMap.containsKey(broadcastDeviceVO.getDeviceId())) {
                //获取设备属性
                DevicePropResponse devicePropResponse = devicePropResponsesMap.get(broadcastDeviceVO.getDeviceId());
                if (!CollectionUtils.isEmpty(devicePropResponse.getPropKeyValues())) {
                    broadcastDeviceVO.setWorkStatus((String) devicePropResponse.getPropKeyValues().getFirst().getPropValue());
                }
            }
        });
    }
}
