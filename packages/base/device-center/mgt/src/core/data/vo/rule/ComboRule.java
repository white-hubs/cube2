package com.guhecloud.cube.iot.mgt.core.data.vo.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * 符合条件的事件,实现复杂暂不支持
 *
 * @author fl
 * @date 2022/11/16
 **/
@Data
public class ComboRule implements Rule {
    private static String json = """
            {
                  "op": "or",
                  "rules": [
                    {
                      "op": "and",
                      "rules": [
                        {
                          "delayTime": 0,
                          "deviceId": "1970697433139675136",
                          "op": "EQ",
                          "oprand": [
                            "99"
                          ],
                          "propCode": "p1"
                        }
                      ]
                    }
                  ]
              }
            """;
    private String op;
    private long delayTime;
    private String deviceId;
    @JsonDeserialize(using = JacksonRuleValueDeserializer.class)
    private List<Rule> rules;

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.COMBO;
    }
}
