package com.guhecloud.cube.iot.mgt.north.websocket;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.core.data.exception.IotParameterInvalidException;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.north.SubscribeInfo;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceCommandCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceEventCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DevicePropCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceStatusCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.RuleTriggerEventCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.SubscribeCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import com.guhecloud.cube.iot.mgt.core.def.SubscribeInterface;
import com.guhecloud.platform.base.util.StringUtils;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType.DEVICE_COMMAND;
import static com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType.DEVICE_EVENT;
import static com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType.DEVICE_PROP;
import static com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType.DEVICE_STATUS;
import static com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType.RULE_TRIGGER_EVENT;

/**
 * 订阅支持<br/>
 */
@Component
@Slf4j
public class SubscribeInterfaceImpl implements SubscribeInterface, DisposableBean {

    // 订阅数量限制
    private static final int MAX_SUBSCRIBE_COUNT = Short.MAX_VALUE; // 最大订阅数量1万

    //集群同步，客户端向单节点订阅，需要通知其他节点
    // 以下KEY为RocketMQ消息体前缀定义,eg: del_subscribe_msg:subscribeId 代表删除订阅信息
    private final HashedWheelTimer hashedWheelTimer;
    private volatile boolean timerStopped = false;
    private final Map<String, SubscribeInfo> subscribeInfoMap;
    private final Map<String, Timeout> timeoutMap;
    /**
     * * deviceId+propCode ===> List<Pair< NotifyOut, OutParam> > <br/>
     * * deviceId+eventCode ===> List<Pair< NotifyOut, OutParam> > <br/>
     * * deviceId+status  ===> List<Pair< NotifyOut, OutParam> > <br/>
     * * commandId ===> List<Pair< NotifyOut, OutParam> > <br/>
     * * ruleId ===> List<Pair< NotifyOut, OutParam> > <br/>
     */
    private final Map<String, Set<WebSocketParam>> cachedSubscribeMapping;
    private final ExecutorService executorService;

    public SubscribeInterfaceImpl(@Qualifier("iotCommonPool") ExecutorService executorService) {
        hashedWheelTimer = new HashedWheelTimer(1, TimeUnit.SECONDS);
        hashedWheelTimer.start();

        subscribeInfoMap = new ConcurrentHashMap<>();
        cachedSubscribeMapping = new ConcurrentHashMap<>();
        timeoutMap = new ConcurrentHashMap<>();

        this.executorService = executorService;
    }

    //<editor-fold desc="SubscribeInterface">
    @Override
    public String subscribe(SubscribeInfo subscribeInfo) {
        // 检查订阅数量是否超过1万
        if (subscribeInfoMap.size() >= MAX_SUBSCRIBE_COUNT) {
            log.error("订阅数量已达上限: {}, 拒绝新的订阅请求", MAX_SUBSCRIBE_COUNT);
            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo),
                                                   "订阅数量已达上限，当前: " + subscribeInfoMap.size() + ", 最大: " + MAX_SUBSCRIBE_COUNT);
        }

        String subscribeId = buildSubscribeId(subscribeInfo);

        appendSubscribeInfo(subscribeInfo);

        subscribeInfoMap.put(subscribeId, subscribeInfo);
        timeoutMap.put(subscribeId, hashedWheelTimer.newTimeout(
                new CancelSubscribeTask(subscribeId, subscribeInfo),
                subscribeInfo.getSubscribeTTS(),
                TimeUnit.SECONDS));

        return subscribeId;
    }

    @Override
    public SubscribeInfo unSubscribe(String subscribeId) {
        //remove memory cache
        SubscribeInfo subscribeInfo = subscribeInfoMap.get(subscribeId);
        if (Objects.nonNull(subscribeInfo)) {
            delSubscribeInfo(subscribeInfo);
            subscribeInfoMap.remove(subscribeId);
        }

        // cancel timer task if not expired
        Timeout timeout = timeoutMap.get(subscribeId);
        if (Objects.nonNull(timeout)) {
            timeoutMap.remove(subscribeId);
            timeout.cancel();
        }

        return subscribeInfo;
    }

    /**
     * 将订阅信息添加到内存中<br/>
     * <p>
     * deviceId+propCode ===> List<Pair< NotifyOut, OutParam> > <br/>
     * deviceId+eventCode ===> List<Pair< NotifyOut, OutParam> > <br/>
     * deviceId+status  ===> List<Pair< NotifyOut, OutParam> > <br/>
     * commandId ===> List<Pair< NotifyOut, OutParam> > <br/>
     * ruleId ===> List<Pair< NotifyOut, OutParam> > <br/>
     * </p>
     *
     * @param subscribeInfo subscribeInfo
     */
    private void appendSubscribeInfo(SubscribeInfo subscribeInfo) {
        if (Objects.isNull(subscribeInfo)) {
            throw new IotParameterInvalidException("null", "订阅信息不能为null");
        }

        List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes = subscribeInfo.getSubscribeMsgTypes();

        if (Objects.isNull(subscribeMsgTypes) || subscribeMsgTypes.isEmpty()) {
            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型不能为空");
        }

        WebSocketParam outParam = subscribeInfo.getOutParam();
        if (Objects.isNull(outParam)) {
            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "输出参数不能为空");
        }

        // 用于记录已添加的映射关系，以便异常时回滚
        List<String> addedKeys = new ArrayList<>();

        try {
            //内存快速查找映射
            // deviceId+propCode ===> List<OutParam>
            // deviceId+eventCode ===> List<OutParam>
            // deviceId+status  ===> List<OutParam>
            // commandId ===> List<OutParam>
            // ruleId ===> List<OutParam>
            for (Pair<NotifyMsgType, SubscribeCondition> subscribeMsgType : subscribeMsgTypes) {
                if (Objects.isNull(subscribeMsgType)) {
                    log.warn("订阅消息类型项为null，跳过该项");
                    continue;
                }

                NotifyMsgType notifyType = subscribeMsgType.getKey();
                SubscribeCondition subscribeCondition = subscribeMsgType.getVal();

                if (Objects.isNull(notifyType) || Objects.isNull(subscribeCondition)) {
                    throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "通知类型或订阅条件不能为null");
                }

                switch (notifyType) {
                    case DEVICE_STATUS:
                        if (!(subscribeCondition instanceof DeviceStatusCondition)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型与订阅条件不匹配");
                        }

                        List<String> deviceIds = ((DeviceStatusCondition) subscribeCondition).getDeviceIds();
                        if (Objects.isNull(deviceIds) || deviceIds.isEmpty()) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "设备id必填");
                        }

                        for (String deviceId : deviceIds) {
                            String key = buildDeviceStatsKey(deviceId);
                            putCachedSubscribeMapping(key, outParam);
                            addedKeys.add(key);
                        }
                        break;
                    case DEVICE_EVENT:
                        //包含事件附件
                        if (!(subscribeCondition instanceof DeviceEventCondition)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型与订阅条件不匹配");
                        }

                        List<String> deviceIdAndEventCode = ((DeviceEventCondition) subscribeCondition).getDeviceIdAndEventCode();
                        if (Objects.isNull(deviceIdAndEventCode) || deviceIdAndEventCode.isEmpty()) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "设备id和事件编码必填");
                        }

                        //deviceId+eventCode
                        for (String deviceIdAndEvent : deviceIdAndEventCode) {
                            String key = buildDeviceEventKey(deviceIdAndEvent);
                            putCachedSubscribeMapping(key, outParam);
                            addedKeys.add(key);
                        }
                        break;
                    case DEVICE_PROP:
                        if (!(subscribeCondition instanceof DevicePropCondition)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型与订阅条件不匹配");
                        }

                        List<String> deviceIdAndPropCode = ((DevicePropCondition) subscribeCondition).getDeviceIdAndPropCode();
                        if (Objects.isNull(deviceIdAndPropCode) || deviceIdAndPropCode.isEmpty()) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "设备id和属性编码必填");
                        }

                        for (String deviceIdAndProp : deviceIdAndPropCode) {
                            String key = buildDevicePropKey(deviceIdAndProp);
                            putCachedSubscribeMapping(key, outParam);
                            addedKeys.add(key);
                        }
                        break;
                    case DEVICE_COMMAND:
                        if (!(subscribeCondition instanceof DeviceCommandCondition)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型与订阅条件不匹配");
                        }

                        String commandId = ((DeviceCommandCondition) subscribeCondition).getCommandId();
                        if (Objects.isNull(commandId)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "命令id必填");
                        }

                        String commandKey = buildDeviceCommandKey(commandId);
                        putCachedSubscribeMapping(commandKey, outParam);
                        addedKeys.add(commandKey);
                        break;
                    case RULE_TRIGGER_EVENT:
                        if (!(subscribeCondition instanceof RuleTriggerEventCondition)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "订阅消息类型与订阅条件不匹配");
                        }

                        String ruleId = ((RuleTriggerEventCondition) subscribeCondition).getRuleId();
                        if (Objects.isNull(ruleId)) {
                            throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "规则id必填");
                        }

                        String ruleKey = buildRuleTriggerEventKey(ruleId);
                        putCachedSubscribeMapping(ruleKey, outParam);
                        addedKeys.add(ruleKey);
                        break;
                    default:
                        throw new IotParameterInvalidException(JSON.toJSONString(subscribeInfo), "未知的订阅消息类型: " + notifyType);
                }
            }
        } catch (Exception e) {
            // 异常时回滚已添加的映射关系
            log.error("添加订阅信息时发生异常，开始回滚操作", e);
            rollbackSubscribeMappings(addedKeys, outParam);
            throw e; // 重新抛出异常
        }
    }

    /**
     * 回滚已添加的订阅映射关系
     *
     * @param keys     需要回滚的key列表
     * @param outParam 输出参数
     */
    private void rollbackSubscribeMappings(List<String> keys, WebSocketParam outParam) {
        for (String key : keys) {
            try {
                delCachedSubscribeMapping(key, outParam);
            } catch (Exception rollbackException) {
                log.error("回滚订阅映射关系时发生异常，key: {}", key, rollbackException);
            }
        }
        log.info("订阅映射关系回滚完成，共回滚 {} 个key", keys.size());
    }

    private void delSubscribeInfo(SubscribeInfo subscribeInfo) {
        if (Objects.isNull(subscribeInfo)) {
            log.warn("订阅信息为null，跳过删除操作");
            return;
        }

        List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes = subscribeInfo.getSubscribeMsgTypes();
        if (Objects.isNull(subscribeMsgTypes) || subscribeMsgTypes.isEmpty()) {
            log.warn("订阅消息类型列表为空，跳过删除操作");
            return;
        }

        WebSocketParam webSocketParam = subscribeInfo.getOutParam();
        if (Objects.isNull(webSocketParam)) {
            log.warn("WebSocket参数为null，跳过删除操作");
            return;
        }

        for (Pair<NotifyMsgType, SubscribeCondition> subscribeMsgType : subscribeMsgTypes) {
            if (Objects.isNull(subscribeMsgType)) {
                log.warn("订阅消息类型项为null，跳过该项");
                continue;
            }

            NotifyMsgType notifyType = subscribeMsgType.getKey();
            SubscribeCondition subscribeCondition = subscribeMsgType.getVal();

            if (Objects.isNull(notifyType) || Objects.isNull(subscribeCondition)) {
                log.warn("通知类型或订阅条件为null，跳过该项");
                continue;
            }

            switch (notifyType) {
                case DEVICE_STATUS:
                    List<String> deviceIds = ((DeviceStatusCondition) subscribeCondition).getDeviceIds();
                    if (Objects.nonNull(deviceIds)) {
                        deviceIds.forEach(deviceId -> delCachedSubscribeMapping(buildDeviceStatsKey(deviceId), webSocketParam));
                    }
                    break;
                case DEVICE_EVENT:
                    //包含事件附件
                    List<String> deviceIdAndEventCode = ((DeviceEventCondition) subscribeCondition).getDeviceIdAndEventCode();

                    //deviceId+eventCode
                    if (Objects.nonNull(deviceIdAndEventCode)) {
                        deviceIdAndEventCode.forEach(deviceIdAndEvent -> delCachedSubscribeMapping(buildDeviceEventKey(deviceIdAndEvent), webSocketParam));
                    }
                    break;
                case DEVICE_PROP:
                    List<String> deviceIdAndPropCode = ((DevicePropCondition) subscribeCondition).getDeviceIdAndPropCode();
                    if (Objects.nonNull(deviceIdAndPropCode)) {
                        deviceIdAndPropCode.forEach(deviceIdAndProp -> delCachedSubscribeMapping(buildDevicePropKey(deviceIdAndProp), webSocketParam));
                    }
                    break;
                case DEVICE_COMMAND:
                    String commandId = ((DeviceCommandCondition) subscribeCondition).getCommandId();
                    if (StringUtils.isNotBlank(commandId)) {
                        delCachedSubscribeMapping(buildDeviceCommandKey(commandId), webSocketParam);
                    }
                    break;
                case RULE_TRIGGER_EVENT:
                    String ruleId = ((RuleTriggerEventCondition) subscribeCondition).getRuleId();
                    if (StringUtils.isNotBlank(ruleId)) {
                        delCachedSubscribeMapping(buildRuleTriggerEventKey(ruleId), webSocketParam);
                    }
                    break;
                default:
                    log.error("未知的订阅消息类型: {}, skip", notifyType);
            }
        }
    }

    private String buildDeviceStatsKey(String deviceId) {
        return DEVICE_STATUS.ordinal() + "." + deviceId + ".status";
    }

    private String buildDeviceEventKey(String deviceIdAndEvent) {
        String[] split = deviceIdAndEvent.split(DeviceEventCondition.SPLIT);
        if (split.length != 2) {
            throw new IotParameterInvalidException(JSON.toJSONString(deviceIdAndEvent), "设备id和事件编码格式错误");
        }

        //deviceId+eventCode
        return DEVICE_EVENT.ordinal() + "." + split[0] + "." + split[1];
    }

    private String buildDevicePropKey(String deviceIdAndProp) {
        String[] split = deviceIdAndProp.split(SubscribeCondition.SPLIT);
        if (split.length != 2) {
            throw new IotParameterInvalidException(JSON.toJSONString(deviceIdAndProp), "设备id和属性编码格式错误");
        }

        //deviceId+propCode
        return DEVICE_PROP.ordinal() + "." + split[0] + "." + split[1];
    }

    private String buildDeviceCommandKey(String commandId) {
        return DEVICE_COMMAND.ordinal() + "." + commandId;
    }

    private String buildRuleTriggerEventKey(String ruleId) {
        return RULE_TRIGGER_EVENT.ordinal() + "." + ruleId;
    }

    /**
     * 向本地缓存中添加订阅信息
     *
     * @param key      缓存key
     * @param outParam 订阅信息
     */
    private void putCachedSubscribeMapping(String key, WebSocketParam outParam) {
        // 使用computeIfAbsent确保线程安全地创建和获取Set
        Set<WebSocketParam> existsCacheRule = cachedSubscribeMapping.computeIfAbsent(key, k -> new HashSet<>());

        // 对Set的操作需要同步，因为HashSet本身不是线程安全的
        synchronized (existsCacheRule) {
            existsCacheRule.add(outParam);
        }
    }

    /**
     * 从本地缓存中移除订阅信息
     *
     * @param key      缓存key
     * @param outParam 订阅信息
     */
    private void delCachedSubscribeMapping(String key, WebSocketParam outParam) {
        //获取指定key的订阅信息集合
        Set<WebSocketParam> existsCacheRule = cachedSubscribeMapping.get(key);

        if (Objects.isNull(existsCacheRule)) {
            //key不存在，无需处理
            return;
        }

        // 对Set的操作需要同步，确保线程安全
        synchronized (existsCacheRule) {
            //从集合中移除指定的订阅信息
            existsCacheRule.remove(outParam);

            //如果集合为空，移除整个key
            if (existsCacheRule.isEmpty()) {
                cachedSubscribeMapping.remove(key);
            }
        }
    }

    /**
     * 构建订阅句柄ID
     *
     * @param subscribeInfo 订阅信息
     *
     * @return 订阅句柄ID
     */
    private String buildSubscribeId(SubscribeInfo subscribeInfo) {
        return "WebSocket." + subscribeInfo.getOutParam().identity();
    }

    //</editor-fold>

    //<editor-fold desc="SubscribeCacheSupport">
    @Override
    public Set<WebSocketParam> queryDeviceProp(String deviceId, String propCode) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(propCode)) {
            return Collections.emptySet();
        }

        Set<WebSocketParam> set = cachedSubscribeMapping.get(buildDevicePropKey(deviceId + "." + propCode));
        if (Objects.isNull(set)) {
            return Collections.emptySet();
        }

        return set;
    }

    @Override
    public Set<WebSocketParam> queryDeviceEvent(String deviceId, String eventCode) {

        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(eventCode)) {
            return Collections.emptySet();
        }

        Set<WebSocketParam> set = cachedSubscribeMapping.get(buildDeviceEventKey(deviceId + "." + eventCode));
        if (Objects.isNull(set)) {
            return Collections.emptySet();
        }

        return set;
    }

    @Override
    public Set<WebSocketParam> queryDeviceStatus(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return Collections.emptySet();
        }

        Set<WebSocketParam> set = cachedSubscribeMapping.get(buildDeviceStatsKey(deviceId));
        if (Objects.isNull(set)) {
            return Collections.emptySet();
        }

        return set;
    }

    @Override
    public Set<WebSocketParam> queryCommand(String commandId) {
        if (StringUtils.isBlank(commandId)) {
            return Collections.emptySet();
        }

        Set<WebSocketParam> set = cachedSubscribeMapping.get(buildDeviceCommandKey(commandId));
        if (Objects.isNull(set)) {
            return Collections.emptySet();
        }

        return set;
    }

    @Override
    public Set<WebSocketParam> queryRule(String ruleId) {
        if (StringUtils.isBlank(ruleId)) {
            return Collections.emptySet();
        }

        Set<WebSocketParam> set = cachedSubscribeMapping.get(buildRuleTriggerEventKey(ruleId));
        if (Objects.isNull(set)) {
            return Collections.emptySet();
        }

        return set;
    }
    //</editor-fold>

    /**
     * Spring Bean销毁时的清理方法
     * 确保HashedWheelTimer资源正确释放
     */
    @Override
    public void destroy() {
        log.info("正在关闭SubscribeInterfaceImpl，清理资源...");

        try {
            if (hashedWheelTimer != null && !timerStopped) {
                // 停止HashedWheelTimer，等待所有任务完成
                hashedWheelTimer.stop();
                timerStopped = true;
                log.info("HashedWheelTimer已成功关闭");
            }
        } catch (Exception e) {
            log.error("关闭HashedWheelTimer时发生异常", e);
        }

        // 清空缓存
        subscribeInfoMap.clear();
        cachedSubscribeMapping.clear();
        timeoutMap.clear();

        log.info("SubscribeInterfaceImpl资源清理完成");
    }

    /**
     * 取消订阅任务
     */
    class CancelSubscribeTask implements TimerTask {

        private final String subscribeId;
        private final SubscribeInfo subscribeInfo;

        public CancelSubscribeTask(String subscribeId, SubscribeInfo subscribeInfo) {
            this.subscribeId = subscribeId;
            this.subscribeInfo = subscribeInfo;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            try {
                // 尝试提交到线程池执行
                executorService.submit(() -> {
                    log.debug("开始清理过期订阅: {}", subscribeId);

                    // 从内存缓存中移除订阅信息
                    subscribeInfoMap.remove(subscribeId);

                    // 清理订阅映射关系
                    delSubscribeInfo(subscribeInfo);

                    log.debug("过期订阅清理完成: {}", subscribeId);
                });
            } catch (RejectedExecutionException e) {
                // 线程池拒绝时，直接在当前线程同步执行清理逻辑
                log.warn("线程池拒绝执行清理任务，改为同步执行清理逻辑: {}", subscribeId);

                try {
                    // 直接执行清理逻辑，确保订阅信息不会泄漏
                    subscribeInfoMap.remove(subscribeId);
                    delSubscribeInfo(subscribeInfo);

                    log.info("同步清理过期订阅完成: {}", subscribeId);
                } catch (Exception cleanupException) {
                    log.error("同步清理订阅信息时发生异常: {}", subscribeId, cleanupException);
                }
            } catch (Exception e) {
                log.error("清理过期订阅时发生未知异常: {}", subscribeId, e);
            }
        }
    }
}
