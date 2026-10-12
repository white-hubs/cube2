package com.guhecloud.cube.customization.xialing.server.liangcang.config;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** 从门禁管理关联设备中解析海康 MAC 地址对应的设备 ID。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HikRemoteCheckDeviceMappingProvider {

    private static final String DOOR_APPLICATION_CODE = "DOORMANAGE";

    private final DataDictionaryClient dataDictionaryClient;
    private final SecurityDeviceClient securityDeviceClient;
    private final IotDevicePropClient iotDevicePropClient;

    public Long resolveDoorDeviceId(String macAddress) {
        if (StringUtils.isBlank(macAddress)) {
            return null;
        }
        try {
            String propertyCode = loadMacPropertyCode();
            if (propertyCode == null) {
                return null;
            }
            List<Long> response = securityDeviceClient.getSecurityDeviceIds(DOOR_APPLICATION_CODE);
            List<Long> associatedDeviceIds = response == null ? List.of() : response.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            if (associatedDeviceIds.isEmpty()) {
                log.info("门禁管理未关联设备，applicationCode={}", DOOR_APPLICATION_CODE);
                return null;
            }
            var param = new MultiDevicePropParam();
            param.setDevIdAndPropCodes(associatedDeviceIds.stream()
                                               .map(deviceId -> deviceId + "." + propertyCode)
                                               .collect(Collectors.joining(",")));
            Map<String, List<KeyValueWithTsDTO>> valuesByDevice = iotDevicePropClient.multiProp(param);
            Long doorDeviceId = associatedDeviceIds.stream()
                    .filter(deviceId -> matches(valuesByDevice == null ? null : valuesByDevice.get(String.valueOf(deviceId)),
                                                propertyCode, macAddress))
                    .findFirst()
                    .orElse(null);
            log.info("从门禁管理关联设备属性匹配海康 MAC，macAddress={}，propertyCode={}，associatedDeviceCount={}，doorDeviceId={}",
                     macAddress, propertyCode, associatedDeviceIds.size(), doorDeviceId);
            return doorDeviceId;
        } catch (RuntimeException exception) {
            log.warn("查询门禁管理关联设备或匹配海康 MAC 失败，macAddress={}", macAddress, exception);
            return null;
        }
    }

    private String loadMacPropertyCode() {
        var query = new DataDictionaryaramByItemAndCodeParam();
        query.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
        query.setCode(TenantDictionary.CODE_HIK_REMOTE_CHECK_MAC_PROP_CODE);
        var dictionary = dataDictionaryClient.getDataDictionaryByItemAndCode(query);
        if (dictionary == null || StringUtils.isBlank(dictionary.getVariable())) {
            log.warn("海康门禁 MAC 属性编码租户字典未配置，code={}",
                     TenantDictionary.CODE_HIK_REMOTE_CHECK_MAC_PROP_CODE);
            return null;
        }
        return dictionary.getVariable().trim();
    }

    private boolean matches(List<KeyValueWithTsDTO> values, String propertyCode, String macAddress) {
        if (values == null) {
            return false;
        }
        String target = normalize(macAddress);
        return values.stream()
                .filter(value -> value != null && propertyCode.equals(value.getKey()))
                .map(KeyValueWithTsDTO::getValue)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .anyMatch(value -> target.equals(normalize(value)));
    }

    private String normalize(String macAddress) {
        return macAddress == null ? "" : macAddress
                .replace(":", "")
                .replace("-", "")
                .replace(" ", "")
                .toLowerCase(Locale.ROOT);
    }
}
