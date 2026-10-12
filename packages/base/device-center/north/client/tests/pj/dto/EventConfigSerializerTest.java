package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.param.ParamSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EventConfigSerializer 单元测试
 *
 * 验证序列化器对数组字段的正确处理：
 * 1. params 为 null 时应输出空数组 []
 * 2. params 为空列表时应输出空数组 []
 * 3. params 有数据时应正确输出数组（不应有嵌套结构）
 * 4. 修复前的问题：params 非空时会生成错误的嵌套结构 {"params": {"params": [...]}}
 */
public class EventConfigSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_ParamsIsNull_ShouldReturnEmptyArray() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());
        eventConfig.setParams(null);

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("params 为 null 时应序列化为空数组 []，实际结果: " + json,
            json.contains("\"params\":[]"));
    }

    @Test
    public void testSerialize_ParamsIsEmpty_ShouldReturnEmptyArray() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());
        eventConfig.setParams(Collections.emptyList());

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("params 为空列表时应序列化为空数组 []，实际结果: " + json,
            json.contains("\"params\":[]"));
        Assert.assertFalse("params 不应该序列化为空对象 {}",
            json.contains("\"params\":{}"));
    }

    @Test
    public void testSerialize_ParamsHasData_ShouldReturnArrayWithoutNesting() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());

        List<ParamSpec> params = new ArrayList<>();
        ParamSpec param = new ParamSpec();
        param.setParamCode("testParam");
        param.setName("测试参数");
        params.add(param);
        eventConfig.setParams(params);

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("params 有数据时应序列化为数组，实际结果: " + json,
            json.contains("\"params\":["));
        Assert.assertTrue("params 应包含参数数据，实际结果: " + json,
            json.contains("\"param_code\":\"testParam\""));

        // 关键验证：确保没有嵌套结构（修复前的 bug）
        Assert.assertFalse("params 不应该有嵌套结构 {\"params\": {\"params\": [...]}}",
            json.contains("\"params\":{\"params\""));

        // 验证正确的结构：params 直接是数组
        int paramsIndex = json.indexOf("\"params\":");
        if (paramsIndex != -1) {
            char nextChar = json.charAt(paramsIndex + "\"params\":".length());
            Assert.assertEquals("params 后面应该直接是数组 [，而不是对象 {",
                '[', nextChar);
        }
    }

    @Test
    public void testSerialize_EventRelatedPropIsNull_ShouldNotOutputField() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(null);
        eventConfig.setParams(Collections.emptyList());

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果：north/client 版本对 eventRelatedProp 为 null 时不输出字段
        // 这是与 mgt 版本的差异
        Assert.assertFalse("eventRelatedProp 为 null 时不应输出该字段，实际结果: " + json,
            json.contains("\"eventRelatedProp\""));
    }

    @Test
    public void testSerialize_EventRelatedPropIsEmpty_ShouldOutputArray() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());
        eventConfig.setParams(Collections.emptyList());

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("eventRelatedProp 为空列表时应输出该字段，实际结果: " + json,
            json.contains("\"eventRelatedProp\""));
    }

    @Test
    public void testSerialize_EventRelatedPropHasData_ShouldReturnArray() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);

        List<String> eventRelatedProp = new ArrayList<>();
        eventRelatedProp.add("prop1");
        eventRelatedProp.add("prop2");
        eventConfig.setEventRelatedProp(eventRelatedProp);
        eventConfig.setParams(Collections.emptyList());

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("eventRelatedProp 有数据时应序列化为数组，实际结果: " + json,
            json.contains("\"eventRelatedProp\":["));
        Assert.assertTrue("eventRelatedProp 应包含数据，实际结果: " + json,
            json.contains("\"prop1\""));
        Assert.assertTrue("eventRelatedProp 应包含数据，实际结果: " + json,
            json.contains("\"prop2\""));
    }

    @Test
    public void testSerialize_CompleteObject_ShouldReturnCorrectJson() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);

        List<String> eventRelatedProp = new ArrayList<>();
        eventRelatedProp.add("temperature");
        eventConfig.setEventRelatedProp(eventRelatedProp);

        List<ParamSpec> params = new ArrayList<>();
        ParamSpec param = new ParamSpec();
        param.setParamCode("threshold");
        param.setName("阈值");
        params.add(param);
        eventConfig.setParams(params);

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("应包含 triggerType 字段",
            json.contains("\"triggerType\":\"NATURE\""));
        Assert.assertTrue("eventRelatedProp 应正确序列化",
            json.contains("\"eventRelatedProp\":[\"temperature\"]"));
        Assert.assertTrue("params 应正确序列化为数组",
            json.contains("\"params\":["));
        Assert.assertTrue("params 应包含参数数据",
            json.contains("\"param_code\":\"threshold\""));

        // 确保没有嵌套结构
        Assert.assertFalse("params 不应该有嵌套结构",
            json.contains("\"params\":{\"params\""));
    }

    @Test
    public void testSerialize_MultipleParams_ShouldReturnCorrectArray() throws Exception {
        // 准备测试数据
        EventConfig eventConfig = new EventConfig();
        eventConfig.setTriggerType(TriggerType.NATURE);
        eventConfig.setEventRelatedProp(Collections.emptyList());

        List<ParamSpec> params = new ArrayList<>();

        ParamSpec param1 = new ParamSpec();
        param1.setParamCode("param1");
        param1.setName("参数1");
        params.add(param1);

        ParamSpec param2 = new ParamSpec();
        param2.setParamCode("param2");
        param2.setName("参数2");
        params.add(param2);

        eventConfig.setParams(params);

        // 执行序列化
        String json = objectMapper.writeValueAsString(eventConfig);

        // 验证结果
        Assert.assertTrue("params 应序列化为数组",
            json.contains("\"params\":["));
        Assert.assertTrue("应包含第一个参数",
            json.contains("\"param_code\":\"param1\""));
        Assert.assertTrue("应包含第二个参数",
            json.contains("\"param_code\":\"param2\""));

        // 验证数组中有两个元素
        int param1Index = json.indexOf("\"param_code\":\"param1\"");
        int param2Index = json.indexOf("\"param_code\":\"param2\"");
        Assert.assertTrue("两个参数应该按顺序出现在数组中",
            param1Index > 0 && param2Index > param1Index);
    }
}
