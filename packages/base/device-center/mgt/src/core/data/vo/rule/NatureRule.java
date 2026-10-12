package com.guhecloud.cube.iot.mgt.core.data.vo.rule;

/**
 * 空规则，仅内部使用，设备原生事件
 *
 * @author fl
 * @date 2022/11/15
 **/
public class NatureRule implements Rule {
    public static final Rule INSTANCE = new NatureRule();

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.NATURE;
    }
}
