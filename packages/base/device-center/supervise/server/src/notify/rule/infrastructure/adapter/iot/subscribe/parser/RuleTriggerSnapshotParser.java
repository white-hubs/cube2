package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe.parser;

import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommonTriggerPart;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.CompareOpEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class RuleTriggerSnapshotParser {

    private final IotDeviceClient iotDeviceClient;

    private final Pattern pattern = Pattern.compile("\\[(.*?)]\\s*(\\w+)\\s*\\[(.*?)]");

    public RuleTriggerSnapshotParser(IotDeviceClient iotDeviceClient) {
        this.iotDeviceClient = iotDeviceClient;
    }

    /**
     * 将规则触发快照列表解析为可读的描述性字符串。
     *
     * @param triggerMsg 触发消息
     *
     * @return 描述性字符串，例如："设备 [设备名] 的属性 [属性名] 的值 [值] 满足条件 [大于 阈值] 且 ..."
     */
    public String parseSnapshot(Long deviceId, RuleTriggerMsg triggerMsg) {
        if (triggerMsg == null || CollectionUtils.isEmpty(triggerMsg.getRuleTriggerSnapshot())) {
            return "无具体触发条件信息";
        }
        //获取设备详情
        var deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(deviceId.toString());
        deviceDetailParam.setWithPropertySpec(true);
        var deviceDetail = this.iotDeviceClient.detail(deviceDetailParam);
        var propCodeToNameMap = deviceDetail.getProductPropVoList().stream()
                .collect(Collectors.toMap(ProductPropDTO::getPropCode, ProductPropDTO::getPropName));
        // 将每个快照对象转换为可读的字符串
        var descriptions = triggerMsg.getRuleTriggerSnapshot().stream()
                .map(snapshot -> parseSingleSnapshot(snapshot, propCodeToNameMap))
                .collect(Collectors.toList());
        return String.join(" 且 ", descriptions);
    }

    /**
     * 解析单个快照对象为可读字符串。
     */
    private String parseSingleSnapshot(CommonTriggerPart snapshot, Map<String, String> propCodeToNameMap) {
        var condition = snapshot.getCondition();
        if (StringUtils.isBlank(condition)) {
            return String.format("设备属性 [%s] 的值 [%s] 满足条件",
                                 snapshot.getPropCode(),
                                 snapshot.getPropValue());
        }

        // 解析条件字符串，例如 "[测试流量检测设备01.currentCarNum] GT [20]"
        // 使用正则表达式提取设备名、属性名、操作符和阈值
        var matcher = pattern.matcher(condition);

        if (matcher.find()) {
            var deviceAndProp = matcher.group(1);  // 测试流量检测设备01.currentCarNum
            var operator = matcher.group(2);       // GT
            var threshold = matcher.group(3);      // 20

            // 分离设备名和属性名
            var deviceName = "未知设备";
            var prodCode = snapshot.getPropCode();
            if (deviceAndProp.contains(".")) {
                var parts = deviceAndProp.split("\\.");
                deviceName = parts[0];
                prodCode = parts.length > 1 ? parts[1] : snapshot.getPropCode();
            } else {
                deviceName = deviceAndProp;
            }
            var operatorInChinese = convertOperator(operator);
            return String.format("设备 [%s] 的属性 [%s] 的值 [%s] %s [%s]",
                                 deviceName,
                                 propCodeToNameMap.getOrDefault(prodCode, prodCode),
                                 snapshot.getPropValue(),
                                 operatorInChinese,
                                 threshold);
        } else {
            return String.format("设备属性 [%s] 的值 [%s] 满足条件 [%s]",
                                 snapshot.getPropCode(),
                                 snapshot.getPropValue(),
                                 condition);
        }
    }

    /**
     * 将操作符代码转换为中文描述。
     */
    private String convertOperator(String operator) {
        if (StringUtils.isBlank(operator)) {
            return "未知操作符";
        }

        try {
            return CompareOpEnum.valueOf(operator).getDescription();
        } catch (IllegalArgumentException e) {
            // 如果操作符不在枚举中，返回原始操作符
            return operator;
        }
    }

}