package com.guhecloud.cube.biz.door.server.service.impl;

import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityClientHandlerServiceImpl {
    private final SecurityDeviceClient securityDeviceClient;

    /**
     * 获取通用模块中关联的门禁设备三方id
     *
     * @param deviceIds 设备业务id集合
     *
     * @return 设备三方系统id集合
     */
    public List<String> getSecurityDoorDeviceThirdIds(List<Long> deviceIds) {
        Set<String> thirdPartyIds = new HashSet<>();
        ModelUnderDeviceParam modelUnderDeviceParam = new ModelUnderDeviceParam();
        modelUnderDeviceParam.setApplicationCode("DOORMANAGE");
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            modelUnderDeviceParam.setModelIds(new ArrayList<>(deviceIds));
        }
        List<SecurityDeviceVo> securityDeviceVos = securityDeviceClient.getDeviceListByModel(modelUnderDeviceParam);
        if (ObjectUtils.isEmpty(securityDeviceVos)) {
            thirdPartyIds.add("-1L");
        } else {
            securityDeviceVos.forEach(sd -> thirdPartyIds.add(sd.getThirdPartyId()));
        }
        return new ArrayList<>(thirdPartyIds);
    }
}
