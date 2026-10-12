package com.guhecloud.cube.iot.mgt.core.data.vo.rule;

import com.guhecloud.cube.iot.mgt.core.data.lang.Entry;
import lombok.Data;

import java.util.List;

/**
 * 阈值设置规则
 *
 * @author fl
 * @date 2022/10/10
 **/
@Data
public class SettingRule implements Rule {
    /**
     * 针对一个设备做阈值设置
     */
    private String deviceId;

    /**
     * 针对一个产品做阈值设置
     */
    private Long productId;

    private List<Entry> setValues;

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.SETTING;
    }
}