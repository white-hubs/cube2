package com.guhecloud.cube.iot.north.bridge.consumer.filter.test;

import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterCriteria;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDeviceStatusChangeHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.CommandResponseMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceStatusHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.RuleEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.BizDeviceStatus;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommonTriggerPart;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceCommandInfoMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceStatusUpdateMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class FilterableHandlerTest {

    private static KeyValueWithTs kv(String key) {
        KeyValueWithTs kv = new KeyValueWithTs();
        kv.setKey(key);
        kv.setValue("v");
        kv.setTs(System.currentTimeMillis());
        return kv;
    }

    /**
     * 创建设备状态消息（用于状态变更测试）。
     *
     * @param deviceId 设备ID
     * @param status   设备状态（ON_LINE/OFF_LINE）
     * @return 设备状态更新消息
     */
    private static DeviceStatusUpdateMsg statusMsg(String deviceId, BizDeviceStatus status) {
        DeviceStatusUpdateMsg msg = new DeviceStatusUpdateMsg();
        msg.setDeviceId(deviceId);
        KeyValueWithTs kv = new KeyValueWithTs();
        kv.setKey("status");
        kv.setValue(status.name());
        kv.setTs(System.currentTimeMillis());
        msg.setUpdates(Collections.singletonList(kv));
        return msg;
    }

    private static CommonTriggerPart triggerPart(String deviceId) {
        CommonTriggerPart part = new CommonTriggerPart();
        part.setDeviceId(deviceId);
        return part;
    }

    @Test
    public void testDeviceProp_emptyCriteria_allPass() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder().build(delegate);

        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d1");
        msg.setUpdates(Collections.singletonList(kv("temp")));

        handler.handle(msg);
        assertEquals(1, count.get());
    }

    @Test
    public void testDeviceProp_deviceIdFilter() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1", "d2")
                        .build(delegate);

        LivePropertyUpdateMsg match = new LivePropertyUpdateMsg();
        match.setDeviceId("d1");
        match.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(match);
        assertEquals(1, count.get());

        LivePropertyUpdateMsg unmatch = new LivePropertyUpdateMsg();
        unmatch.setDeviceId("d9");
        unmatch.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(unmatch);
        assertEquals(1, count.get());
    }

    @Test
    public void testDeviceProp_paramFilter_and_fieldLevelFilter() {
        AtomicReference<LivePropertyUpdateMsg> received = new AtomicReference<>();
        DevicePropMsgHandler delegate = received::set;

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .propCodes("temp")
                        .build(delegate);

        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d1");
        msg.setUpdates(Arrays.asList(kv("temp"), kv("hum")));

        handler.handle(msg);

        assertNotNull(received.get());
        assertEquals(1, msg.getUpdates().size());
        assertEquals("temp", msg.getUpdates().get(0).getKey());
    }

    @Test
    public void testDeviceProp_deviceAndParam_combination() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1")
                        .propCodes("temp")
                        .build(delegate);

        LivePropertyUpdateMsg pass = new LivePropertyUpdateMsg();
        pass.setDeviceId("d1");
        pass.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(pass);
        assertEquals(1, count.get());

        LivePropertyUpdateMsg deviceMismatch = new LivePropertyUpdateMsg();
        deviceMismatch.setDeviceId("d2");
        deviceMismatch.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(deviceMismatch);
        assertEquals(1, count.get());

        LivePropertyUpdateMsg paramMismatch = new LivePropertyUpdateMsg();
        paramMismatch.setDeviceId("d1");
        paramMismatch.setUpdates(Collections.singletonList(kv("hum")));
        handler.handle(paramMismatch);
        assertEquals(1, count.get());
    }

    @Test
    public void testDeviceEvent_filters() {
        AtomicInteger count = new AtomicInteger();
        DeviceEventMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDeviceEventMsgHandler handler =
                FilterableDeviceEventMsgHandler.builder()
                        .deviceIds("d1")
                        .eventCodes("alarm")
                        .build(delegate);

        DeviceEventTriggerMsg pass = new DeviceEventTriggerMsg();
        pass.setDeviceId("d1");
        pass.setEventCode("alarm");
        handler.handle(pass);
        assertEquals(1, count.get());

        DeviceEventTriggerMsg deviceMismatch = new DeviceEventTriggerMsg();
        deviceMismatch.setDeviceId("d2");
        deviceMismatch.setEventCode("alarm");
        handler.handle(deviceMismatch);
        assertEquals(1, count.get());

        DeviceEventTriggerMsg codeMismatch = new DeviceEventTriggerMsg();
        codeMismatch.setDeviceId("d1");
        codeMismatch.setEventCode("other");
        handler.handle(codeMismatch);
        assertEquals(1, count.get());
    }

    // ========== 动态管理测试用例 ==========

    @Test
    public void testDynamicAddDeviceId() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(delegate);

        // d1应该通过
        LivePropertyUpdateMsg msg1 = new LivePropertyUpdateMsg();
        msg1.setDeviceId("d1");
        msg1.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(msg1);
        assertEquals(1, count.get());

        // d2不应该通过
        LivePropertyUpdateMsg msg2 = new LivePropertyUpdateMsg();
        msg2.setDeviceId("d2");
        msg2.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(msg2);
        assertEquals(1, count.get());

        // 动态添加d2
        handler.addDeviceId("d2");

        // 现在d2应该通过
        handler.handle(msg2);
        assertEquals(2, count.get());
    }

    @Test
    public void testDynamicAddDeviceIds() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(delegate);

        // 批量添加设备
        handler.addDeviceIds(Arrays.asList("d2", "d3", "d4"));

        // 验证所有设备都能通过
        for (String deviceId : Arrays.asList("d1", "d2", "d3", "d4")) {
            LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
            msg.setDeviceId(deviceId);
            msg.setUpdates(Collections.singletonList(kv("temp")));
            handler.handle(msg);
        }

        assertEquals(4, count.get());
    }

    @Test
    public void testDynamicRemoveDeviceId() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1", "d2", "d3")
                        .build(delegate);

        // d2应该通过
        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d2");
        msg.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(msg);
        assertEquals(1, count.get());

        // 移除d2
        handler.removeDeviceId("d2");

        // 现在d2不应该通过
        handler.handle(msg);
        assertEquals(1, count.get());

        // d1和d3仍然应该通过
        LivePropertyUpdateMsg msg1 = new LivePropertyUpdateMsg();
        msg1.setDeviceId("d1");
        msg1.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(msg1);
        assertEquals(2, count.get());
    }

    @Test
    public void testDynamicClearDeviceIds() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1", "d2")
                        .propCodes("temp")
                        .build(delegate);

        // d3不应该通过（不在设备列表中）
        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d3");
        msg.setUpdates(Collections.singletonList(kv("temp")));
        handler.handle(msg);
        assertEquals(0, count.get());

        // 清空设备ID过滤
        handler.clearDeviceIds();

        // 现在d3应该通过（只要有temp属性）
        handler.handle(msg);
        assertEquals(1, count.get());
    }

    @Test
    public void testDynamicAddParamCode() {
        AtomicReference<LivePropertyUpdateMsg> received = new AtomicReference<>();
        DevicePropMsgHandler delegate = received::set;

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .propCodes("temp")
                        .build(delegate);

        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d1");
        msg.setUpdates(Arrays.asList(kv("temp"), kv("hum")));

        handler.handle(msg);
        assertNotNull(received.get());
        assertEquals(1, received.get().getUpdates().size());
        assertEquals("temp", received.get().getUpdates().get(0).getKey());

        // 动态添加hum属性
        handler.addParamCode("hum");

        // 重新处理消息
        msg.setUpdates(Arrays.asList(kv("temp"), kv("hum")));
        received.set(null);
        handler.handle(msg);

        assertNotNull(received.get());
        assertEquals(2, received.get().getUpdates().size());
    }

    @Test
    public void testDynamicRemoveParamCode() {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .propCodes("temp", "hum")
                        .build(delegate);

        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
        msg.setDeviceId("d1");
        msg.setUpdates(Collections.singletonList(kv("temp")));

        handler.handle(msg);
        assertEquals(1, count.get());

        // 移除temp属性
        handler.removeParamCode("temp");

        // 现在temp不应该通过
        handler.handle(msg);
        assertEquals(1, count.get());

        // hum仍然应该通过
        msg.setUpdates(Collections.singletonList(kv("hum")));
        handler.handle(msg);
        assertEquals(2, count.get());
    }

    @Test
    public void testRuntimeFilterAdjustment() {
        // 测试运行时动态调整过滤条件
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .propCodes("temp")
                        .build(delegate);

        LivePropertyUpdateMsg msg1 = new LivePropertyUpdateMsg();
        msg1.setDeviceId("d1");
        msg1.setUpdates(Collections.singletonList(kv("temp")));

        LivePropertyUpdateMsg msg2 = new LivePropertyUpdateMsg();
        msg2.setDeviceId("d2");
        msg2.setUpdates(Collections.singletonList(kv("hum")));

        // 初始状态：只有msg1通过
        handler.handle(msg1);
        handler.handle(msg2);
        assertEquals(1, count.get());

        // 添加d2和hum
        handler.addDeviceId("d2");
        handler.addParamCode("hum");

        // 现在msg2也应该通过
        handler.handle(msg2);
        assertEquals(2, count.get());

        // 移除d1
        handler.removeDeviceId("d1");

        // 现在msg1不应该通过
        handler.handle(msg1);
        assertEquals(2, count.get());
    }

    @Test
    public void testConcurrentAddRemoveDeviceIds() throws Exception {
        // 测试并发添加和移除设备ID
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .build(delegate);

        int threads = 10;
        int operationsPerThread = 100;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            final int threadId = i;
            pool.submit(() -> {
                try {
                    start.await();
                    for (int j = 0; j < operationsPerThread; j++) {
                        String deviceId = "d_" + threadId + "_" + j;
                        handler.addDeviceId(deviceId);
                        handler.removeDeviceId(deviceId);
                    }
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS));
        pool.shutdownNow();

        // 验证没有异常抛出，操作都是线程安全的
    }

    @Test
    public void testConcurrentFilterModification() throws Exception {
        // 测试并发修改过滤条件和处理消息
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(delegate);

        int threads = 8;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        // 一半线程处理消息，一半线程修改过滤条件
        for (int i = 0; i < threads; i++) {
            final int threadId = i;
            pool.submit(() -> {
                try {
                    start.await();
                    if (threadId % 2 == 0) {
                        // 处理消息
                        for (int j = 0; j < 1000; j++) {
                            LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
                            msg.setDeviceId("d1");
                            msg.setUpdates(Collections.singletonList(kv("temp")));
                            handler.handle(msg);
                        }
                    } else {
                        // 修改过滤条件
                        for (int j = 0; j < 100; j++) {
                            handler.addDeviceId("d" + j);
                            handler.addParamCode("p" + j);
                            handler.removeDeviceId("d" + j);
                            handler.removeParamCode("p" + j);
                        }
                    }
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS));
        pool.shutdownNow();

        // 验证没有异常，所有操作都是线程安全的
        assertTrue(count.get() > 0);
    }

    @Test
    public void testAddNullDeviceId() {
        // 测试添加null值应该被忽略
        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(msg -> {});

        handler.addDeviceId(null);
        handler.addDeviceIds(null);

        assertThrows(IllegalArgumentException.class, () -> handler.addDeviceIds(Arrays.asList("d2", null, "d3")));
    }

    @Test
    public void testAddDuplicateDeviceId() {
        // 测试添加重复值
        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(msg -> {});

        handler.addDeviceId("d1");
        handler.addDeviceId("d1");

        FilterCriteria criteria = handler.getFilterCriteria();
        Set<String> deviceIds = criteria.getDeviceIdsSnapshot();
        assertEquals(1, deviceIds.size());
    }

    @Test
    public void testRemoveNonExistentDeviceId() {
        // 测试移除不存在的值
        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceId("d1")
                        .build(msg -> {});

        handler.removeDeviceId("d999");
        handler.removeDeviceIds(Arrays.asList("d888", "d777"));

        // 验证不会抛出异常
        FilterCriteria criteria = handler.getFilterCriteria();
        Set<String> deviceIds = criteria.getDeviceIdsSnapshot();
        assertEquals(1, deviceIds.size());
        assertTrue(deviceIds.contains("d1"));
    }

    @Test
    public void testGetFilterCriteria() {
        // 测试获取FilterCriteria对象
        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1", "d2")
                        .propCodes("temp", "hum")
                        .build(msg -> {});

        FilterCriteria criteria = handler.getFilterCriteria();
        assertNotNull(criteria);

        // 通过FilterCriteria直接操作
        criteria.addDeviceId("d3");
        criteria.addParamCode("pressure");

        Set<String> deviceIds = criteria.getDeviceIdsSnapshot();
        Set<String> paramCodes = criteria.getParamCodesSnapshot();

        assertEquals(3, deviceIds.size());
        assertEquals(3, paramCodes.size());
        assertTrue(deviceIds.contains("d3"));
        assertTrue(paramCodes.contains("pressure"));
    }

    @Test
    public void testConcurrentFiltering_noException() throws Exception {
        AtomicInteger count = new AtomicInteger();
        DevicePropMsgHandler delegate = msg -> count.incrementAndGet();

        FilterableDevicePropMsgHandler handler =
                FilterableDevicePropMsgHandler.builder()
                        .deviceIds("d1")
                        .propCodes("temp")
                        .build(delegate);

        int threads = 8;
        int perThread = 1000;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int j = 0; j < perThread; j++) {
                        LivePropertyUpdateMsg msg = new LivePropertyUpdateMsg();
                        msg.setDeviceId("d1");
                        msg.setUpdates(Collections.singletonList(kv("temp")));
                        handler.handle(msg);
                    }
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS));
        pool.shutdownNow();

        assertEquals(threads * perThread, count.get());
    }

    // ========== 设备状态变更测试用例 ==========

    @Test
    public void testDeviceStatusChange_firstStatusTriggers() {
        // 测试：首次收到状态消息时应该触发回调
        AtomicInteger count = new AtomicInteger();

        FilterableDeviceStatusChangeHandler handler =
                FilterableDeviceStatusChangeHandler.builder()
                        .build((deviceId, isOnline) -> count.incrementAndGet());

        // 首次收到设备在线消息
        handler.handle(statusMsg("d1", BizDeviceStatus.ON_LINE));
        assertEquals(1, count.get());
    }

    @Test
    public void testDeviceStatusChange_deviceIdFilter() {
        // 测试：设备ID过滤功能
        AtomicInteger count = new AtomicInteger();
        List<String> deviceLog = new ArrayList<>();

        FilterableDeviceStatusChangeHandler handler =
                FilterableDeviceStatusChangeHandler.builder()
                        .deviceIds("d1", "d2")
                        .build((deviceId, isOnline) -> {
                            count.incrementAndGet();
                            deviceLog.add(deviceId);
                        });

        // d1 在线 - 应触发
        handler.handle(statusMsg("d1", BizDeviceStatus.ON_LINE));
        assertEquals(1, count.get());

        // d2 在线 - 应触发
        handler.handle(statusMsg("d2", BizDeviceStatus.ON_LINE));
        assertEquals(2, count.get());

        // d3 在线 - 不应触发（不在过滤列表中）
        handler.handle(statusMsg("d3", BizDeviceStatus.ON_LINE));
        assertEquals(2, count.get());

        // d1 离线 - 应触发
        handler.handle(statusMsg("d1", BizDeviceStatus.OFF_LINE));
        assertEquals(3, count.get());

        // 验证只处理了d1和d2
        assertEquals(Arrays.asList("d1", "d2", "d1"), deviceLog);
    }

    @Test
    public void testDeviceStatusChange_nullDeviceIdIgnored() {
        // 测试：deviceId为null的消息应被忽略
        AtomicInteger count = new AtomicInteger();

        FilterableDeviceStatusChangeHandler handler =
                FilterableDeviceStatusChangeHandler.builder()
                        .build((deviceId, isOnline) -> count.incrementAndGet());

        DeviceStatusUpdateMsg nullDeviceMsg = statusMsg("d1", BizDeviceStatus.ON_LINE);
        nullDeviceMsg.setDeviceId(null);

        handler.handle(nullDeviceMsg);
        assertEquals(0, count.get());
    }
}
