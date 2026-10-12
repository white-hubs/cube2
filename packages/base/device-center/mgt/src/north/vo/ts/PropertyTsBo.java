package com.guhecloud.cube.iot.mgt.north.vo.ts;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mioopp
 * @create 2020-12-14 13:57
 */
@Setter
@Getter
public class PropertyTsBo {

    private String propertyCode;

    private String propertyName;

    private String dataType;

    private String propertyUnit;

    @JSONField(serialize = false)
    private String dataConfig;

    private JSONArray dataConstraint;

    private List<KeyValueWithTs> valueTsBoList;
}
